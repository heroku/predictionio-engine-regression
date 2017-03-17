package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class DataSourceTest
  extends FlatSpec with SharedSparkContext with SharedStorageContext with IsolatedAppData with Matchers {

  ignore should "return the data" in {
    val dataSource = new DataSource(
      new DataSourceParams())
    val data = dataSource.readTraining(sc = sparkContext)
    data shouldBe a [TrainingData]
  }
}