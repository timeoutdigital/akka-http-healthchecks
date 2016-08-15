package com.timeout.healthchecks

import java.net.{InetSocketAddress, Socket}

import cats.data.{NonEmptyList, Validated}

object HealthChecks {

  def ping(host: String, port: Int): HealthCheck = healthCheck(s"Ping $host:$port", () => {
    Validated.catchNonFatal {
      new Socket().connect(new InetSocketAddress(host, port), 250)
      ()
    }.leftMap(t => NonEmptyList(t.getMessage))
  })
}
