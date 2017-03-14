package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ServingTest
  extends FlatSpec with Matchers {

  val query = Query(vector = Array(5))
  val predictedResults = Seq(
    PredictedResult(label = 25),
    PredictedResult(label = 50),
    PredictedResult(label = 75))

  "serve" should "return the first prediction" in {
    val serving = new Serving()
    val prediction = serving.serve(
      query = query,
      predictedResults = predictedResults)
    prediction shouldBe a [PredictedResult]
    prediction.label shouldEqual 25
  }
}