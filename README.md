# [PredictionIO](https://predictionio.incubator.apache.org) regression engine for [Heroku](http://www.heroku.com) 

🚧 **Work in progress** 🚧 

A machine learning app deployable to Heroku with the [PredictionIO buildpack](https://github.com/heroku/predictionio-buildpack). Use [Spark's Linear Regression algorithm](https://spark.apache.org/docs/1.6.3/mllib-linear-methods.html#regression) to predict a label from a vector of values.


## Demo Story 🐸

This engine demonstrates prediction of a student's **class grade** based on their grade on an **aptitude test**. The model is trained with a small, example data set.

Five **students'** aptitude and class grades are provided in the [included training data](data/).


## Sample Query

```bash
curl -X "POST" "http://localhost:8000/queries.json" \
     -H "Content-Type: application/json; charset=utf-8" \
     -d $'{"vector": [ 80 ]}'
```

Sample Response:

```json
{"label": 78.32954840770623}
```


## Notes

```bash

# Make sure PredictionIO's `conf/pio_env.sh` values reference your local installation
SPARK_HOME=$PIO_HOME/vendors/spark-1.6.3-bin-hadoop2.6
POSTGRES_JDBC_DRIVER=$PIO_HOME/vendors/postgresql-9.4.1209.jar

$ pio status

$ pio app new regress

$ pio import --appid 1 --input data/initial-events.json

$ pio build

$ PIO_EVENTSERVER_APP_NAME=regress pio train

$ PIO_EVENTSERVER_APP_NAME=regress pio deploy
```