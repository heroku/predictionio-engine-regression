package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import org.apache.predictionio.data.storage.PropertyMap
import org.joda.time.DateTime

class PreparatorTest
  extends FlatSpec with SharedSparkContext with Matchers {

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

  "prepare" should "return the labeled vectors" in {
    val dataSourceRDD = sparkContext.parallelize(dataSource)
    val preparedData = new PreparedData(values = dataSourceRDD)
    preparedData shouldBe a [PreparedData]
  }
}