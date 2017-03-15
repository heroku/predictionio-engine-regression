package org.template.regression

import org.apache.predictionio.controller.AverageMetric
import org.apache.predictionio.controller.EmptyEvaluationInfo
import org.apache.predictionio.controller.EngineParams
import org.apache.predictionio.controller.EngineParamsGenerator
import org.apache.predictionio.controller.Evaluation

import org.apache.predictionio.core.BaseEngine
import org.apache.predictionio.controller.Metric

case class MeanSquaredError()
  extends AverageMetric[EmptyEvaluationInfo, Query, PredictedResult, ActualResult] {

  def calculate(query: Query, predicted: PredictedResult, actual: ActualResult): Double = {
    val errorValue = math.pow((actual.label - predicted.label), 2)
    if (errorValue.isNaN) {
      // Ignore uncalculable errors.
      Double.NaN
    } else if (actual.label > 0 && predicted.label < 0
        || actual.label < 0 && predicted.label > 0
    ) {
      // Ignore predictions with inverse sign.
      Double.NaN
    } else {
      errorValue
    }
  }

  // Smallest, numeric MeanSquaredError value will be the superior choice!
  override
  def compare(r0: Double, r1: Double): Int = {
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

object MeanSquaredErrorEvaluation extends Evaluation {
  // type shenanigans to satisfy the compiler
  engineMetric = (
    RegressionEngine().asInstanceOf[
      BaseEngine[EmptyEvaluationInfo, Query, PredictedResult, Serializable]],
    MeanSquaredError().asInstanceOf[
      Metric[EmptyEvaluationInfo, Query, PredictedResult, Serializable, _]])
}

object EngineParamsList extends EngineParamsGenerator {
  private[this] val baseEP = EngineParams(
    dataSourceParams = DataSourceParams())

  engineParamsList = Seq(
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 1000.0     )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 5000.0     )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 100.0      )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 50.0       )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 10.0       )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 5.0        )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 1.0        )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.5        )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.1        )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.05       )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.01       )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.005      )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.001      )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.0005     )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.0001     )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.00005    )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.00001    )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.000005   )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.000001   )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.0000005  )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.0000001  )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.00000005 )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.00000001 )))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.000000005)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(100, 0.000000001))))
  )
}
