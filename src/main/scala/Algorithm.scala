package org.template.regression

import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.predictionio.controller.Params

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
    val labeledPoints: RDD[LabeledPoint] = data.values.map {
      case (entityId, properties) =>
        LabeledPoint(properties.get[Double]("label"),
          Vectors.dense(
            properties.get[Array[Double]]("vector")))
    }
    .cache

    LinearRegressionWithSGD.train(
      labeledPoints,
      ap.numIterations,
      ap.stepSize)
  }

  def predict(model: LinearRegressionModel, query: Query): PredictedResult = {
    val features = Vectors.dense(query.vector)
    val label = model.predict(features)
    new PredictedResult(label)
  }
}
