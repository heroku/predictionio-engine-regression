package org.template.regression

import org.apache.predictionio.controller.PPreparator
import org.apache.predictionio.data.storage.PropertyMap
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class Preparator extends PPreparator[TrainingData, PreparedData] {
  def prepare(sc: SparkContext, trainingData: TrainingData): PreparedData = {
    PreparedData(trainingData.values)
  }
}

case class PreparedData(values: RDD[(String, PropertyMap)])
