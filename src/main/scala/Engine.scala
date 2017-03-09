package org.template.regression

import org.apache.predictionio.controller.IEngineFactory
import org.apache.predictionio.controller.Engine

case class Query(x: Double) extends Serializable

case class PredictedResult(y: Double) extends Serializable

object RegressionEngineFactory extends IEngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("algo" -> classOf[Algorithm]),
      classOf[Serving])
  }
}
