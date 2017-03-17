package org.template.regression

import org.scalatest.{BeforeAndAfterAll, Suite}

import org.h2.tools.Server
import org.apache.predictionio.data.storage.Storage

trait SharedStorageContext extends BeforeAndAfterAll {
  this: Suite =>

  private var _h2Server: Option[Server] = None

  override def beforeAll() {
    _h2Server = Some(Server.createTcpServer("-tcpPort", "9092"))
    _h2Server.get.start

    super.beforeAll()
  }

  override def afterAll() {
    super.afterAll()

    _h2Server match {
      case Some(server) => server.stop
      case None => None
    }
    _h2Server = None
  }
}