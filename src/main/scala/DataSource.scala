package org.template.regression

import org.apache.predictionio.controller.PDataSource
import org.apache.predictionio.controller.EmptyEvaluationInfo
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.store.PEventStore
import org.apache.predictionio.data.storage.PropertyMap

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors
import org.json4s._

import grizzled.slf4j.Logger

case class DataSourceParams() extends Params

class DataSource(val dsp: DataSourceParams)
  extends PDataSource[TrainingData,
      EmptyEvaluationInfo, Query, ActualResult] {

  @transient lazy val logger = Logger[this.type]

  override
  def readTraining(sc: SparkContext): TrainingData = {
    new TrainingData(values = selectEvents(sc))
  }

  override
  def readEval(sc: SparkContext): Seq[(TrainingData, EmptyEvaluationInfo, RDD[(Query, ActualResult)])] = {
    val events = selectEvents(sc)
    Seq(
      ( new TrainingData(values = events),
        new EmptyEvaluationInfo(),
        events.map { case (entityId, properties) =>
          (
            new Query(properties.get[Array[Double]]("vector")),
            new ActualResult(properties.get[Double]("label"))
          )
        })
    )
  }

  def selectEvents(sc: SparkContext): RDD[(String, PropertyMap)] = {
    PEventStore.aggregateProperties(
        appName = sys.env("PIO_EVENTSERVER_APP_NAME"),
        entityType = "student",
        required = Some(List("aptitude-grade", "overall-grade"))
      )(sc)
      .map { case (entityId, properties) =>
        (entityId, new PropertyMap(
          fields = Map(
            "vector" -> JArray(List(
              JDouble(properties.get[Double]("aptitude-grade"))
            )),
            "label" -> JDouble(properties.get[Double]("overall-grade"))),
          firstUpdated = properties.firstUpdated,
          lastUpdated = properties.lastUpdated))
      }
      .cache
  }
}

class TrainingData(
  val values: RDD[(String, PropertyMap)]
) extends Serializable {
  override def toString = {
    s"values: [${values.count()}] (${values.take(2).toList}...)"
  }
}
