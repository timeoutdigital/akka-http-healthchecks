package com.timeout.healthchecks

import java.net.{InetSocketAddress, Socket, URL}

import cats.data.{NonEmptyList, Validated}

object HealthChecks {

  private def doPing(host: String, port: Int) = {
    Validated.catchNonFatal {
      new Socket().connect(new InetSocketAddress(host, port), 250)
    }.leftMap(t => NonEmptyList(t.getMessage))
  }

  def ping(host: String, port: Int): HealthCheck = healthCheck(s"Ping $host:$port", () => doPing(host, port))

  def ping(url: String): HealthCheck = healthCheck(s"Ping $url", () => {

    def defaultPort(protocol: String) = protocol match {
      case "https" => 443
      case _ => 80
    }

    def portOrDefault(url: URL) = if (url.getPort < 0) defaultPort(url.getProtocol) else url.getPort

    val parsedUrl = new URL(url)
    doPing(parsedUrl.getHost, portOrDefault(parsedUrl))
  })
}
