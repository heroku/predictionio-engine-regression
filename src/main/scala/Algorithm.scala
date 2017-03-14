package org.template.regression

import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.BiMap

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import grizzled.slf4j.Logger

import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

case class AlgorithmParams(numIterations: Int, stepSize: Double) extends Params

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, LinearRegressionModel, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): LinearRegressionModel = {

    logger.info(s"~~~~~ AlgorithmParams ${ap}")

    val labelMap = BiMap.stringDouble(data.values.map(_._1))

    val labeledPoints: RDD[LabeledPoint] = data.values.map {
      case (entityId, properties) =>
        LabeledPoint(properties.get[Double]("label"),
          Vectors.dense(
            properties.get[Array[Double]]("vector")))
    }
    .cache

    val labels = labeledPoints.take(5).map {
      (v) => s"${v.label}=>${v.features}"
    }
    logger.info(s"~~~~~ LabeledPoints: ${labels.mkString(", ")}")

    val model = LinearRegressionWithSGD.train(
      labeledPoints,
      ap.numIterations,
      ap.stepSize)

    logger.info(s"~~~~~ Model: ${model}")

    val valuesAndPreds = labeledPoints.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    val preds = valuesAndPreds.take(5).map {
      (v) => s"${v._1}=>${v._2}"
    }
    logger.info(s"~~~~~ Label=>Prediction: ${preds.mkString(", ")}")

    val MSE = valuesAndPreds.map{case(v, p) => math.pow((v - p), 2)}.mean()
    logger.info("~~~~~ Mean Squared Error: " + MSE)

    model
  }

  def predict(model: LinearRegressionModel, query: Query): PredictedResult = {
    val features = Vectors.dense(query.vector)
    val label = model.predict(features)
    new PredictedResult(label)
  }
}
