package org.template.regression

import org.apache.predictionio.controller.LServing

class Serving extends LServing[Query, PredictedResult] {
  override def serve(query: Query, predictedResults: Seq[PredictedResult]): PredictedResult = {

    val isoPR : PredictedResult = predictedResults.find(_.isotonicPrediction.isDefined) match {
      case Some(pr) => pr
      case _ => new PredictedResult()
    }

    val sgdPR : PredictedResult = predictedResults.find(_.sgdPrediction.isDefined) match {
      case Some(pr) => pr
      case _ => new PredictedResult()
    }

    val dtPR : PredictedResult = predictedResults.find(_.decisionTreePrediction.isDefined) match {
      case Some(pr) => pr
      case _ => new PredictedResult()
    }
    
    val ridgePR : PredictedResult = predictedResults.find(_.ridgePrediction.isDefined) match {
      case Some(pr) => pr
      case _ => new PredictedResult()  
    }

    val lassoPR : PredictedResult = predictedResults.find(_.lassoPrediction.isDefined) match {
      case Some(pr) => pr
      case _ => new PredictedResult()
    }

    val isotonicPrediction : Double  = isoPR.isotonicPrediction.getOrElse(0)
    val sgdPrediction : Double  = sgdPR.sgdPrediction.getOrElse(0)
    val decisionTreePrediction : Double  = dtPR.decisionTreePrediction.getOrElse(0)
    val ridgePrediction : Double  = ridgePR.ridgePrediction.getOrElse(0)
    val lassoPrediction : Double  = lassoPR.lassoPrediction.getOrElse(0)
    
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
