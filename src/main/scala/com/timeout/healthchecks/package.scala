package com.timeout

import cats.data._
import cats.syntax.validated._
import cats.std.list._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

package object healthchecks {

  class HealthCheck(val name: String, check: => Future[CheckResult]) {

    private val addNameToErrorMessage: String => String = m => s"$name failed: $m"

    def run()(implicit ec: ExecutionContext): Future[CheckResult] = {
      check.recover {
        case t: Throwable => t.getMessage.invalidNel[Unit]
      }.map(_.leftMap(r => r.map(addNameToErrorMessage)))
    }
  }

  val healthy = ().validNel[String]

  def healthCheck(name: String)(c: => CheckResult): HealthCheck = new HealthCheck(name, Future.fromTry(Try(c)))
  def asyncHealthCheck(name: String)(c: => Future[CheckResult]): HealthCheck = new HealthCheck(name, c)

  type CheckResult = ValidatedNel[String, Unit]

}