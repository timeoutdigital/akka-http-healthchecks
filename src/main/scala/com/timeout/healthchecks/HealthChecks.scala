package com.timeout.healthchecks

import java.net.{InetSocketAddress, Socket}

import akka.http.scaladsl.model.Uri
import cats.data.{NonEmptyList, Validated}
import java.sql.DriverManager

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

  private def doDatabasePing(host: String, port: Int, username: String, password: String) = {
    Validated.catchNonFatal {
      val connection = DriverManager.getConnection(f"jdbc:mysql://$host:$port/mysql?enabledTLSProtocols=TLSv1.2", username, password)
      val statement = connection.createStatement()
      statement.executeQuery("/* ping */ SELECT 1")
      connection.close()
    }.leftMap(t => NonEmptyList.of(t.getMessage()))
  }

  def pingDataBase(host: String, port: Int, username: String, password: String) = healthCheck(s"Ping database $host:$port") {
    doDatabasePing(host, port, username, password)
  }

  def ping(host: String, port: Int, timeout: Int = defaultTimeout): HealthCheck = healthCheck(s"Ping $host:$port") {
    doPing(host, port)
  }

  def pingUrl(url: String, timeout: Int = defaultTimeout): HealthCheck = healthCheck(s"Ping $url") {
    val parsedUri = Uri(url)
    doPing(parsedUri.authority.host.address(), parsedUri.effectivePort)
  }
}
