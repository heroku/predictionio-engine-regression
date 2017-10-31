package org.template.regression

import org.apache.predictionio.controller.LServing

class Serving extends LServing[Query, PredictedResult] {
  override def serve(query: Query, predictedResults: Seq[PredictedResult]): PredictedResult = {

    def fetchPR(pr: Option[PredictedResult]) : PredictedResult = pr match {
        case Some(pr) => pr
        case _ => new PredictedResult()
    }

    val isoPR   : PredictedResult = fetchPR(predictedResults.find(_.isotonicPrediction.isDefined))
    val sgdPR   : PredictedResult = fetchPR(predictedResults.find(_.sgdPrediction.isDefined))
    val dtPR    : PredictedResult = fetchPR(predictedResults.find(_.decisionTreePrediction.isDefined))
    val ridgePR : PredictedResult = fetchPR(predictedResults.find(_.ridgePrediction.isDefined))
    val lassoPR : PredictedResult = fetchPR(predictedResults.find(_.lassoPrediction.isDefined))

    val isotonicPrediction     : Double  = isoPR.isotonicPrediction.getOrElse(0)
    val sgdPrediction          : Double  = sgdPR.sgdPrediction.getOrElse(0)
    val decisionTreePrediction : Double  = dtPR.decisionTreePrediction.getOrElse(0)
    val ridgePrediction        : Double  = ridgePR.ridgePrediction.getOrElse(0)
    val lassoPrediction        : Double  = lassoPR.lassoPrediction.getOrElse(0)
    
    val average : Double = Array(
      isotonicPrediction,
      sgdPrediction,
      decisionTreePrediction,
      ridgePrediction,
      lassoPrediction).sum / predictedResults.length
    
    PredictedResult(
      average=Some(average),
      sgdPrediction=Some(sgdPrediction),
      decisionTreePrediction=Some(decisionTreePrediction),
      isotonicPrediction=Some(isotonicPrediction),
      lassoPrediction=Some(lassoPrediction),
      ridgePrediction=Some(ridgePrediction)
    )
  }
}
