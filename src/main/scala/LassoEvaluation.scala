package org.template.regression

import org.apache.predictionio.controller.{AverageMetric, EmptyEvaluationInfo, EngineParams, EngineParamsGenerator, Evaluation}

case class LassoMeanSquaredError()
  extends AverageMetric[EmptyEvaluationInfo, Query, PredictedResult, ActualResult] {

  def calculate(query: Query, pr: PredictedResult, actual: ActualResult): Double = {
    val predicted : Double = pr.prediction

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

object LassoMeanSquaredErrorEvaluation extends Evaluation {
  engineMetric = (RegressionEngine(), LassoMeanSquaredError())
}

object LassoEngineParamsList extends EngineParamsGenerator {
  private[this] val baseEP = EngineParams(
    dataSourceParams =  DataSourceParams()
  )
  engineParamsList = Seq(
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(100, 0.001, 1))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(150, 0.001, 1))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 1))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(250, 0.001, 1))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(300, 0.001, 1))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(400, 0.001, 1))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(100, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(150, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(250, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(300, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(400, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 2))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 3))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 4))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 5))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 6))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 7))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 8))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 9))),
    EngineParams(algorithmParamsList = Seq("lasso" -> LassoParams(200, 0.001, 10)))
  )
}
