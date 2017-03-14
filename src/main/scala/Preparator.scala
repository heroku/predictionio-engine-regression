package org.template.regression

import org.apache.predictionio.controller.PPreparator
import org.apache.predictionio.data.storage.PropertyMap

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

class Preparator
  extends PPreparator[TrainingData, PreparedData] {

  def prepare(sc: SparkContext, trainingData: TrainingData): PreparedData = {
    new PreparedData(values = trainingData.values)
  }
}

class PreparedData(
  val values: RDD[(String, PropertyMap)]
) extends Serializable
