package org.template.regression

import org.apache.predictionio.controller.{EmptyEvaluationInfo, Engine, EngineFactory}

case class Query(vector: Array[Double])
case class PredictedResult(
  sgdPrediction: Option[Double] = None,
  decisionTreePrediction: Option[Double] = None,
  isotonicPrediction: Option[Double] = None,
  ridgePrediction: Option[Double] = None,
  lassoPrediction: Option[Double] = None,
  average: Option[Double] = None
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
        "tree" -> classOf[DecisionTreeRegression],
        "iso" -> classOf[IsotonicRegressionAlgorithm],
        "ridge" -> classOf[RidgeRegression],
        "lasso" -> classOf[LassoRegression]
      ),
      classOf[Serving]
    )
  }
}
