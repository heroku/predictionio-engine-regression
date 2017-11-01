package org.template.regression

import org.apache.predictionio.controller.LServing

class Serving extends LServing[Query, PredictedResult] {
  override def serve(query: Query, predictedResults: Seq[PredictedResult]): PredictedResult = {
    val average : Double = predictedResults.map(pr => pr.prediction).sum / predictedResults.length
    PredictedResult(average)
  }
}
