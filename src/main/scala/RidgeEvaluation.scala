package org.template.regression

import org.apache.predictionio.controller.{AverageMetric, EmptyEvaluationInfo, EngineParams, EngineParamsGenerator, Evaluation}

case class RidgeMeanSquaredError()
  extends AverageMetric[EmptyEvaluationInfo, Query, PredictedResult, ActualResult] {

  def calculate(query: Query, pr: PredictedResult, actual: ActualResult): Double = {
    val predicted : Double = pr.ridgePrediction.getOrElse(0)

    val errorValue = math.pow(actual.label - predicted, 2)
    if (actual.label > 0 && predicted < 0 || actual.label < 0 && predicted > 0) {
      // Ignore predictions with inverse sign.
      Double.NaN
    }
    else {
      errorValue
    }
  }

  // Smallest, numeric MeanSquaredError value will be the superior choice!
  override def compare(r0: Double, r1: Double): Int = {
    if (r0.isNaN && !r1.isNaN) {
      -1
    } else if (!r0.isNaN && r1.isNaN) {
      1
    } else if (r0.isNaN && r1.isNaN) {
      0
    } else if (r0 > r1) {
      -1
    } else if (r0 < r1) {
      1
    } else {
      0
    }
  }
}

object RidgeMeanSquaredErrorEvaluation extends Evaluation {
  engineMetric = (RegressionEngine(), RidgeMeanSquaredError())
}

object RidgeEngineParamsList extends EngineParamsGenerator {
  private[this] val baseEP = EngineParams(
    dataSourceParams =  DataSourceParams()
  )
  engineParamsList = Seq(
    5000.0, 1000.0, 100.0, 50.0, 10.0, 5.0, 1.0,
    0.5, 0.1, 0.05, 0.01, 0.005, 0.001, 0.0005, 0.0001, 0.00005, 0.00001,
    0.000005, 0.000001, 0.0000005, 0.000001, 0.00000005, 0.00000001
  ).map { stepSize =>
    val algorithmParams = RidgeParams(200, stepSize, 1)
    EngineParams(algorithmParamsList = Seq("ridge" -> algorithmParams))
  }
}
