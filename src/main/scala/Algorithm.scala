package org.template.regression

import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.predictionio.controller.Params

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import grizzled.slf4j.Logger

import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.linalg.Vectors

case class AlgorithmParams(numIterations: Int, stepSize: Double) extends Params

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, LinearRegressionModel, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): LinearRegressionModel = {
    val model = LinearRegressionWithSGD.train(
      data.labeledPoints,
      ap.numIterations,
      ap.stepSize)

    val valuesAndPreds = data.labeledPoints.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    model
  }

  def predict(model: LinearRegressionModel, query: Query): PredictedResult = {
    val features = Vectors.dense(
      Array(query.x)
    )
    val y = model.predict(features)
    new PredictedResult(y)
  }
}
