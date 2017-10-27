package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.apache.predictionio.controller.{EmptyParams}

class DataSourceTest
  extends FlatSpec with SharedSingletonContext with Matchers {

  ignore should "return the data" in {
    val dataSource = new DataSource(
      new EmptyParams())
    val data = dataSource.readTraining(sc = sparkContext)
    data shouldBe a [TrainingData]
  }
}