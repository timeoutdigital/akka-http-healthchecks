package com.timeout.healthchecks.routes

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.data.Validated.{Invalid, Valid}
import com.timeout.healthchecks._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}

object HealthRoutes {

  case class HealthResult(status: String, checks: Seq[String], failures: Seq[String])

  def status(s: Boolean): String = if (s) "healthy" else "unhealthy"
  def statusCode(s: Boolean): StatusCode = if (s) OK else InternalServerError

  def health(checks: HealthCheck*)(implicit ec: ExecutionContext): Route = path("health") {
    complete {
      Future.traverse(checks.toList){ c => c.run().map(c.severity -> _) }.map { r =>
        val healthy = r.forall(_._2.isValid)
        val fatal = r.exists { case (sev, res) => sev.fatal && !res.isValid }
        val checkNames = checks.map(_.name)
        val errorMessages = r.map(_._2).flatMap {
          case Invalid(errs) => errs.toList
          case Valid(_) => List()
        }
        (statusCode(!fatal), HealthResult(status(healthy), checkNames, errorMessages))
      }
    }
  }

}
