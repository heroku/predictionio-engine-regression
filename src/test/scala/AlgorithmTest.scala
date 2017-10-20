package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import org.apache.predictionio.data.storage.PropertyMap
import org.joda.time.DateTime

import org.apache.spark.mllib.regression.LinearRegressionModel
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

class AlgorithmTest
  extends FlatSpec with SharedSingletonContext with Matchers {

  val params = AlgorithmParams(numIterations = 100, stepSize = 0.0004)
  val algorithm = new Algorithm(params)
  val now = new DateTime
  val dataSource = Seq(
    ("1", PropertyMap(
      js = """{ "label": 85.0, "vector": [95.0] }""",
      firstUpdated = now,
      lastUpdated = now)),
    ("2", PropertyMap(
      js = """{ "label": 95.0, "vector": [85.0] }""",
      firstUpdated = now,
      lastUpdated = now)),
    ("3", PropertyMap(
      js = """{ "label": 70.0, "vector": [80.0] }""",
      firstUpdated = now,
      lastUpdated = now)),
    ("4", PropertyMap(
      js = """{ "label": 65.0, "vector": [70.0] }""",
      firstUpdated = now,
      lastUpdated = now)),
    ("5", PropertyMap(
      js = """{ "label": 70.0, "vector": [60.0] }""",
      firstUpdated = now,
      lastUpdated = now)))

  "train" should "return a model" in {
    val dataSourceRDD = sparkContext.parallelize(dataSource)
    val preparedData = new PreparedData(values = dataSourceRDD)
    val model = algorithm.train(sparkContext, preparedData)
    model shouldBe a [LinearRegressionModel]

    val features = Vectors.dense(
      Array(80.0)
    )
    val y = model.predict(features)
    y shouldEqual 78.32954840770623
    //            78.32954840770623
    
  }
}