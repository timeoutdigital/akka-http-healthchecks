package com.timeout

import cats.data._
import cats.syntax.all._
import cats.std.all._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

package object healthchecks {

  class HealthCheck(val name: String, check: AsyncCheck) {

    val addNameToErrorMessage: String => String = m => s"$name failed: $m"

    def run()(implicit ec: ExecutionContext) = {
      check().recover {
        case t: Throwable => t.getMessage.invalidNel[Unit]
      }.map(_.leftMap(r => r.map(addNameToErrorMessage)))
    }
  }

  def healthCheck(name: String, c: Check) = new HealthCheck(name, () => Future.fromTry(Try(c())))
  def asyncHealthCheck(name: String, c: AsyncCheck) = new HealthCheck(name, c)

  type Check = () => ValidatedNel[String, Unit]
  type AsyncCheck = () => Future[ValidatedNel[String, Unit]]

}