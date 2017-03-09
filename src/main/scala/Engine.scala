package org.template.regression

import org.apache.predictionio.controller.IEngineFactory
import org.apache.predictionio.controller.Engine

case class Query(q: String) extends Serializable

case class PredictedResult(p: String) extends Serializable

object RegressionEngineFactory extends IEngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("algo" -> classOf[Algorithm]),
      classOf[Serving])
  }
}
