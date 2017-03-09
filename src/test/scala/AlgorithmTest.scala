package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

class AlgorithmTest
  extends FlatSpec with SharedSingletonContext with Matchers {

  val params = AlgorithmParams(numIterations = 100, stepSize = 0.00000001)
  val algorithm = new Algorithm(params)
  val dataSource = Seq(
    LabeledPoint(1.0, Vectors.dense(Array(95.0,85.0))),
    LabeledPoint(2.0, Vectors.dense(Array(85.0,95.0))),
    LabeledPoint(3.0, Vectors.dense(Array(80.0,70.0))),
    LabeledPoint(4.0, Vectors.dense(Array(70.0,65.0))),
    LabeledPoint(5.0, Vectors.dense(Array(60.0,70.0))))

  "train" should "return a model" in {
    val dataSourceRDD = sparkContext.parallelize(dataSource)
    val preparedData = new PreparedData(labeledPoints = dataSourceRDD)
    val model = algorithm.train(sparkContext, preparedData)
    model shouldBe a [LinearRegressionModel]
  }
}