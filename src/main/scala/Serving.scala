package org.template.regression

import org.apache.predictionio.controller.LServing

class Serving extends LServing[Query, PredictedResult] {
  override def serve(query: Query, predictedResults: Seq[PredictedResult]): PredictedResult = {
    val average : Double = predictedResults.map( p =>
      if (p.SGDPrediction.isDefined) p.SGDPrediction.get else p.DecisionTreePrediction.get
    ).sum / predictedResults.length

    val sgdPrediction = predictedResults.find(_.SGDPrediction.isDefined).get.SGDPrediction
    val decisionTreePrediction = predictedResults.find(_.DecisionTreePrediction.isDefined).get.DecisionTreePrediction
    PredictedResult(Average=Some(average),SGDPrediction=sgdPrediction,DecisionTreePrediction=decisionTreePrediction)
  }
}
