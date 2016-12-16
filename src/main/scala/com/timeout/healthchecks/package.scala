package com.timeout

import cats.data._
import cats.syntax.validated._
import com.timeout.healthchecks.HealthChecks.Severity
import com.timeout.healthchecks.HealthChecks.Severity.Fatal

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

package object healthchecks {

  class HealthCheck(val name: String, check: => Future[CheckResult], val severity: Severity = Fatal) {

    private val addNameToErrorMessage: String => String = m => s"$name failed: $m"

    def run()(implicit ec: ExecutionContext): Future[CheckResult] = {
      check.recover {
        case t: Throwable => t.getMessage.invalidNel[Unit]
      }.map(_.leftMap(r => r.map(addNameToErrorMessage)))
    }
  }

  val healthy: CheckResult = ().validNel[String]

  def healthCheck(name: String, severity: Severity = Fatal)(c: => CheckResult): HealthCheck = new HealthCheck(name, Future.fromTry(Try(c)), severity)
  def asyncHealthCheck(name: String, severity: Severity = Fatal)(c: => Future[CheckResult]): HealthCheck = new HealthCheck(name, c, severity)

  type CheckResult = ValidatedNel[String, Unit]

}