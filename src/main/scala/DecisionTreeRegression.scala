package org.template.regression

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.P2LAlgorithm
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint}
import org.apache.spark.rdd.RDD
import org.apache.predictionio.controller.Params
import org.apache.predictionio.data.storage.PropertyMap

import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils


case class DecisionTreeParams(impurity: String, maxDepth: Int, maxBins: Int) extends Params

class DecisionTreeRegression(val ap: DecisionTreeParams)
  extends P2LAlgorithm[PreparedData, DecisionTreeModel, Query, PredictedResult] {

  @transient lazy val logger: Logger = Logger[this.type]

  override def train(sc: SparkContext, data: PreparedData): DecisionTreeModel = {
    def toLabelPoint(item: (String, PropertyMap)): LabeledPoint = item match {
      case (_, properties) =>
        val label = properties.get[Double]("label")
        val vectors = Vectors.dense(properties.get[Array[Double]]("vector"))
        LabeledPoint(label, vectors)
    }
    val labeledPoints: RDD[LabeledPoint] = data.values.map(toLabelPoint).cache
    val categoricalFeaturesInfo = Map[Int, Int]()
    DecisionTree.trainRegressor(labeledPoints, categoricalFeaturesInfo, ap.impurity, ap.maxDepth, ap.maxBins)
  }

  override def predict(model: DecisionTreeModel, query: Query): PredictedResult = {
    val features = Vectors.dense(query.vector)
    val prediction = model.predict(features)
    PredictedResult(prediction)
  }
}
