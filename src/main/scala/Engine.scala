package org.template.regression

import org.apache.predictionio.controller.{EmptyEvaluationInfo, Engine, EngineFactory}

case class Query(vector: Array[Double])
case class PredictedResult(
  SGDPrediction: Option[Double],
  DecisionTreePrediction: Option[Double],
  Average: Option[Double]
)


case class ActualResult(label: Double)

object RegressionEngine extends EngineFactory {
  type Type = Engine[TrainingData, EmptyEvaluationInfo, PreparedData, Query, PredictedResult, ActualResult]

  def apply(): Type = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map(
        "sgd" -> classOf[LinearRegressionWithSGD],
        "tree" -> classOf[DecisionTreeRegression]),
      classOf[Serving]
    )
  }
}
