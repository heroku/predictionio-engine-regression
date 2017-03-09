package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

class PreparatorTest
  extends FlatSpec with SharedSingletonContext with Matchers {

  val dataSource = Seq(
    LabeledPoint(1.0, Vectors.dense(Array(95.0,85.0))),
    LabeledPoint(2.0, Vectors.dense(Array(85.0,95.0))),
    LabeledPoint(3.0, Vectors.dense(Array(80.0,70.0))),
    LabeledPoint(4.0, Vectors.dense(Array(70.0,65.0))),
    LabeledPoint(5.0, Vectors.dense(Array(60.0,70.0))))

  "prepare" should "return the labeled points" in {
    val dataSourceRDD = sparkContext.parallelize(dataSource)
    val preparedData = new PreparedData(labeledPoints = dataSourceRDD)
    preparedData shouldBe a [PreparedData]
  }
}