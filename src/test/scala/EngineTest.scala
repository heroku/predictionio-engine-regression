package org.template.regression

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import org.apache.predictionio.controller.Engine

class EngineTest
  extends FlatSpec with Matchers {

  "apply" should "return a new engine instance" in {
    val engine = RegressionEngineFactory.apply()
    engine shouldBe an [Engine[_,_,_,_,_,_]]
  }
}