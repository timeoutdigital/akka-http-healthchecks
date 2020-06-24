package com.timeout.healthchecks

import java.net.{InetSocketAddress, Socket}

import akka.http.scaladsl.model.Uri
import cats.data.{NonEmptyList, Validated}

object HealthChecks {
  sealed trait Severity {
    val fatal: Boolean
  }
  trait Fatal extends Severity {
    val fatal = true
  }

  trait NonFatal extends Severity {
    val fatal = false
  }

  object Severity {
    case object Fatal extends Fatal
    case object NonFatal extends NonFatal
  }

  private val defaultTimeout = 1000

  private def doPing(host: String, port: Int, timeout: Int = defaultTimeout) = {
    Validated.catchNonFatal {
      new Socket().connect(new InetSocketAddress(host, port), timeout)
    }.leftMap(t => NonEmptyList.of(t.getMessage))
  }

  def ping(host: String, port: Int, timeout: Int = defaultTimeout): HealthCheck = healthCheck(s"Ping $host:$port") {
    doPing(host, port)
  }

  def pingUrl(url: String, timeout: Int = defaultTimeout): HealthCheck = healthCheck(s"Ping $url") {
    val parsedUri = Uri(url)
    doPing(parsedUri.authority.host.address(), parsedUri.effectivePort)
  }
}
