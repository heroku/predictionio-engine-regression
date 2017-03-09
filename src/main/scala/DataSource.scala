package org.template.regression

import org.apache.predictionio.controller.PDataSource
import org.apache.predictionio.controller.EmptyEvaluationInfo
import org.apache.predictionio.controller.EmptyActualResult
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.Event
import org.apache.predictionio.data.store.PEventStore

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

import grizzled.slf4j.Logger

case class DataSourceParams(appName: String) extends Params

class DataSource(val dsp: DataSourceParams)
  extends PDataSource[TrainingData,
      EmptyEvaluationInfo, Query, EmptyActualResult] {

  @transient lazy val logger = Logger[this.type]

  override
  def readTraining(sc: SparkContext): TrainingData = {
    val pointsRDD: RDD[LabeledPoint] = PEventStore.aggregateProperties(
        appName = sys.env("PIO_EVENTSERVER_APP_NAME"),
        entityType = "user",
        required = Some(List("x", "y"))
      )(sc)
      .map { case (entityId, properties) =>
        try {
          LabeledPoint(properties.get[Double]("label"),
            Vectors.dense(Array(
              properties.get[Double]("x"),
              properties.get[Double]("y")
            ))
          )
        } catch {
          case e: Exception => {
            logger.error(s"Failed to get properties ${properties} of" +
              s" ${entityId}. Exception: ${e}.")
            throw e
          }
        }
      }

    new TrainingData(pointsRDD)
  }
}

class TrainingData(
  val labeledPoints: RDD[LabeledPoint]
) extends Serializable {
  override def toString = {
    s"labeledPoints: [${labeledPoints.count()}] (${labeledPoints.take(2).toList}...)"
  }
}
