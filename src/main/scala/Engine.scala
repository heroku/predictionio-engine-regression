package org.template.regression

import org.apache.predictionio.controller.EngineFactory
import org.apache.predictionio.controller.Engine

case class Query(vector: Array[Double]) extends Serializable

case class PredictedResult(label: Double) extends Serializable
case class ActualResult(label: Double) extends Serializable

object RegressionEngine extends EngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("algo" -> classOf[Algorithm]),
      classOf[Serving])
  }
}
