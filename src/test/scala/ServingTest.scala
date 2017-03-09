package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ServingTest
  extends FlatSpec with Matchers {

  val query = Query(x = 5)
  val predictedResults = Seq(
    PredictedResult(y = 25),
    PredictedResult(y = 50),
    PredictedResult(y = 75))

  "serve" should "return the first prediction" in {
    val serving = new Serving()
    val prediction = serving.serve(
      query = query,
      predictedResults = predictedResults)
    prediction shouldBe a [PredictedResult]
    prediction.y shouldEqual 25
  }
}