package org.template.regression

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterEach, Suite}

trait SharedSparkContext extends BeforeAndAfterEach {
  this: Suite =>

  private var _sparkContext: Option[SparkContext] = None
  def sparkContext = _sparkContext.get
  val sparkConf = new SparkConf(false)

  override def beforeEach() {
    _sparkContext = Some(new SparkContext("local", "test", sparkConf))
    super.beforeEach()
  }

  override def afterEach() {
    super.afterEach()
    sparkContext.stop()
    _sparkContext = None
    System.clearProperty("spark.driver.port")
  }
}