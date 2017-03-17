package org.template.regression

import org.scalatest.{BeforeAndAfterEach, Suite}

import org.h2.tools.Server
import org.apache.predictionio.tools.console.{App, AppArgs, ConsoleArgs}
import org.apache.predictionio.data.storage.Storage

trait IsolatedAppData extends BeforeAndAfterEach {
  this: Suite =>

  override def beforeEach() {
    App.create(ConsoleArgs(app = AppArgs(
      id = Some(sys.env("PIO_EVENTSERVER_APP_ID").toInt),
      name = sys.env("PIO_EVENTSERVER_APP_NAME"),
      description = Option("temporary database for unit tests"))))
    
    // Hack to initialize connection; avoids "Connection pool is not yet initialized" error.
    Storage.getPEvents()

    super.beforeEach()
  }

  override def afterEach() {
    try {
      super.afterEach()
    } finally {  
      App.delete(ConsoleArgs(app = AppArgs(
        id = Some(sys.env("PIO_EVENTSERVER_APP_ID").toInt))))
    }
  }
}
