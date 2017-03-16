package org.template.regression

import grizzled.slf4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.{Params, P2LAlgorithm}
import org.apache.predictionio.data.storage.{DataMap, Event, PropertyMap, Storage}
import org.apache.predictionio.data.store.Common
import org.joda.time.DateTime
import org.json4s._

case class AlgorithmParams(numIterations: Int, stepSize: Double) extends Params

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, LinearRegressionModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): LinearRegressionModel = {
    def toLabelPoint(item: (String, PropertyMap)): LabeledPoint = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
        val vectors = Vectors.dense(properties.get[Array[Double]]("vector"))
        LabeledPoint(label, vectors)
    }
    def toQueryVector(item: (String, PropertyMap)): Vector = item match {
      case (_, properties) =>
        Vectors.dense(properties.get[Array[Double]]("vector"))
    }

    val labeledPoints: RDD[LabeledPoint] = data.values.map(toLabelPoint).cache

    val model = LinearRegressionWithSGD.train(
      labeledPoints,
      ap.numIterations,
      ap.stepSize
    )

    val queryVectors: RDD[Vector] = data.values.map(toQueryVector)

    val predictions = model.predict(queryVectors)
    val dataWithPredictions: RDD[(Double, (String,PropertyMap))] = predictions.zip(data.values)
    exportToPostgres(dataWithPredictions,sc)

    model
  }

  override def predict(model: LinearRegressionModel, query: Query): PredictedResult = {
    val features = Vectors.dense(query.vector)
    val label = model.predict(features)
    PredictedResult(label)
  }

  def exportToPostgres(dataWithPredictions: RDD[(Double, (String, PropertyMap))], sc: SparkContext){
    val appId = sys.env("PIO_EVENTSERVER_APP_ID")
    val now = DateTime.now

    val uuid = java.util.UUID.randomUUID.toString
    val events: RDD[Event] = dataWithPredictions.map { r => {
      new Event(
        eventId           = None,
        event             = "$set",
        entityType        = "training",
        entityId          = uuid,
        targetEntityType  = Some("prediction"),
        targetEntityId    = Some(r._2._1),
        properties        = r._2._2 ++ new DataMap(Map( "prediction" -> JDouble(r._1) )),
        eventTime         = now,
        tags              = Nil,
        prId              = None,
        creationTime      = now)
      } 
    }

    val eventServer = Storage.getPEvents()
    eventServer.write(events,appId.toInt)(sc)
  }
}
