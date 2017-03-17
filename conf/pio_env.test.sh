#!/usr/bin/env bash

# PredictionIO Storage Configuration for tests
#
# This section controls programs that make use of PredictionIO's built-in
# storage facilities. Default values are shown below.
#
# For more information on storage configuration please refer to
# http://predictionio.incubator.apache.org/system/anotherdatastore/

export PIO_EVENTSERVER_APP_NAME=regress
export PIO_EVENTSERVER_APP_ID=1 # always first in tests

# Storage Repositories

export PIO_STORAGE_REPOSITORIES_METADATA_NAME=pio_meta
export PIO_STORAGE_REPOSITORIES_METADATA_SOURCE=H2

export PIO_STORAGE_REPOSITORIES_EVENTDATA_NAME=pio_event
export PIO_STORAGE_REPOSITORIES_EVENTDATA_SOURCE=H2

export PIO_STORAGE_REPOSITORIES_MODELDATA_NAME=pio_model
export PIO_STORAGE_REPOSITORIES_MODELDATA_SOURCE=H2

# Storage Data Sources

# Embedded H2 database
export PIO_STORAGE_SOURCES_H2_TYPE=jdbc
export PIO_STORAGE_SOURCES_H2_URL="jdbc:h2:tcp://localhost:9092/mem:test;DB_CLOSE_DELAY=-1"
export PIO_STORAGE_SOURCES_H2_USERNAME=sa
export PIO_STORAGE_SOURCES_H2_PASSWORD=
