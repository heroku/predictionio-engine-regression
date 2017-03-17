# [PredictionIO](https://predictionio.incubator.apache.org) regression engine for [Heroku](http://www.heroku.com) 

A machine learning app deployable to Heroku with the [PredictionIO buildpack](https://github.com/heroku/predictionio-buildpack). Use [Spark's Linear Regression algorithm](https://spark.apache.org/docs/1.6.3/mllib-linear-methods.html#regression) to predict a label from a vector of values.


## Demo Story 🐸

This engine demonstrates prediction of a student's **class grade** based on their grade on an **aptitude test**. The model is trained with a small, example data set.

Five **students'** aptitude and class grades are provided in the [included training data](data/).


## How To 📚

✏️ Throughout this document, code terms that start with `$` represent a value (shell variable) that should be replaced with a customized value, e.g `$EVENTSERVER_NAME`, `$ENGINE_NAME`, `$POSTGRES_ADDON_ID`…

### Deploy to Heroku

Please follow steps in order.

1. [Requirements](#1-requirements)
1. [Eventserver](#2-eventserver)
   1. [Create the eventserver](#create-the-eventserver)
   1. [Deploy the eventserver](#deploy-the-eventserver)
1. [Regression engine](#3-regression-engine)
   1. [Create the engine](#create-the-engine)
   1. [Connect the engine with the eventserver](#connect-the-engine-with-the-eventserver)
   1. [Import data](#import-data)
   1. [Deploy the engine](#deploy-the-engine)
   1. [Scale-up](#scale-up)
   1. [Retry release](#retry-release)
   1. [Evaluation](#evaluation)

### Usage

Once deployed, how to work with the engine.

* 🎯 [Query for predictions](#query-for-predictions)
* [Diagnostics](#diagnostics)


# Deploy to Heroku 🚀

## 1. Requirements

* [Heroku account](https://signup.heroku.com)
* [Heroku CLI](https://toolbelt.heroku.com), command-line tools
* [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

## 2. Eventserver

### Create the eventserver

⚠️ **An eventserver may host data for multiple engines.** If you already have one provisioned, you may skip to the [engine](#3-regression-engine).

```bash
git clone \
  https://github.com/heroku/predictionio-buildpack.git \
  pio-eventserver

cd pio-eventserver

heroku create $EVENTSERVER_NAME
heroku addons:create heroku-postgresql:hobby-dev
# Note the buildpacks differ for eventserver & engine (below)
heroku buildpacks:add -i 1 https://github.com/heroku/predictionio-buildpack.git
heroku buildpacks:add -i 2 heroku/scala
```

### Deploy the eventserver

We delay deployment until the database is ready.

```bash
heroku pg:wait && git push heroku master
```


## 3. Regression Engine

### Create the engine

```bash
git clone \
  https://github.com/heroku/predictionio-engine-regression.git \
  pio-engine-regress

cd pio-engine-regress

heroku create $ENGINE_NAME
# Note the buildpacks differ for eventserver (above) & engine
heroku buildpacks:add -i 1 https://github.com/heroku/heroku-buildpack-jvm-common.git
heroku buildpacks:add -i 2 https://github.com/heroku/predictionio-buildpack.git
```

### Connect the engine with the eventserver

```bash
# Find the Postgres add-on ID for the eventserver.
heroku addons --app $EVENTSERVER_NAME
#
# Example: `heroku-postgresql (postgresql-aerodynamic-00000)`
#   `postgresql-aerodynamic-00000` is the add-on ID.
#
heroku addons:attach $POSTGRES_ADDON_ID

# Then preset the Eventserver app name & key,
heroku config:set \
  PIO_EVENTSERVER_APP_NAME=regress \
  PIO_EVENTSERVER_ACCESS_KEY=$RANDOM-$RANDOM-$RANDOM-$RANDOM-$RANDOM
```

### Import data

Initial training data is automatically imported from [`data/initial-events.json`](data/initial-events.json).

👓 When you're ready to begin working with your own data, see [data import methods in CUSTOM docs](https://github.com/heroku/predictionio-buildpack/blob/master/CUSTOM.md#import-data).

### Deploy the engine

```bash
git push heroku master

# Follow the logs to see training & web start-up
#
heroku logs -t
```

⚠️ **Initial deploy will probably fail due to memory constraints.** Proceed to scale up.

## Scale up

Once deployed, scale up the processes and config Spark to avoid memory issues. These are paid, [professional dyno types](https://devcenter.heroku.com/articles/dyno-types#available-dyno-types):

```bash
heroku ps:scale \
  web=1:Standard-2X \
  release=0:Performance-L \
  train=0:Performance-L
```

## Retry release

When the release (`pio train`) fails due to memory constraints or other transient error, you may use the Heroku CLI [releases:retry plugin](https://github.com/heroku/heroku-releases-retry) to rerun the release without pushing a new deployment:

```bash
# First time, install it.
heroku plugins:install heroku-releases-retry

# Re-run the release & watch the logs
heroku releases:retry
heroku logs -t
```

## Evaluation

This engine is already setup for PredictionIO's [hyperparamter tuning](https://predictionio.incubator.apache.org/evaluation/paramtuning/).

```bash
heroku run bash --size Performance-L
$ cd pio-engine/
$ pio eval \
    org.template.regression.MeanSquaredErrorEvaluation \
    org.template.regression.EngineParamsList \
    -- $PIO_SPARK_OPTS
```

✏️ Memory parameters are set to fit the [dyno `--size`](https://devcenter.heroku.com/articles/dyno-types#available-dyno-types) set in the `heroku run` command.

# Usage ⌨️

## Query for predictions

The linear regression model should only be queried with values within the training range, `60` to `95` for the sample data.

```bash
curl -X "POST" "http://$ENGINE_NAME.herokuapp.com/queries.json" \
     -H "Content-Type: application/json; charset=utf-8" \
     -d $'{"vector": [ 80 ]}'
```

Sample Response:

```json
{"label": 78.32954840770623}
```

## Diagnostics

If you hit any snags with the engine serving queries, check the logs:

```bash
heroku logs -t --app $ENGINE_NAME
```

If errors are occuring, sometimes a restart will help:

```bash
heroku restart --app $ENGINE_NAME
```

## Testing

[ScalaTest](http://www.scalatest.org) is used for [unit tests](tests/scala/).

Run the test suite with the test environment:

```bash
source conf/pio_env.test.sh && sbt test
```

You may need to locate `sbt` on your system. The PredictionIO distribution contains it at `sbt/sbt`.
