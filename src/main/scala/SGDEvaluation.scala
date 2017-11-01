package org.template.regression

import org.apache.predictionio.controller.{AverageMetric, EmptyEvaluationInfo, EngineParams, EngineParamsGenerator, Evaluation}

case class SGDMeanSquaredError()
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

object SGDMeanSquaredErrorEvaluation extends Evaluation {
  engineMetric = (RegressionEngine(), SGDMeanSquaredError())
}

object SGDEngineParamsList extends EngineParamsGenerator {
  private[this] val baseEP = EngineParams(
    dataSourceParams =  DataSourceParams()
  )
  engineParamsList = Seq(
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(210, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(220, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(230, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(240, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(300, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(400, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(240, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(250, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(260, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(270, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(280, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(290, 0.000066))),
    EngineParams(algorithmParamsList = Seq("sgd" -> AlgorithmParams(200, 0.000067)))
  )
}
