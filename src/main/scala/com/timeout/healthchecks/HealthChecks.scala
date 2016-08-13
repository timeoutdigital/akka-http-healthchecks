package com.timeout.healthchecks

import java.net.{Socket, URL}

import cats.syntax.all._

import scala.util.Try

object HealthChecks {

  def ping(host: String, port: Int) = healthCheck(s"Ping $host:$port", () => {
    Try {
      new Socket(host, port)
      ().validNel[String]
    }.recover {
      case t: Throwable => t.getMessage.invalidNel
    }.get
  })

}
