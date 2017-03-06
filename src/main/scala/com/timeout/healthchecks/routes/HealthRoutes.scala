package com.timeout.healthchecks.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import cats.data.Validated.{Invalid, Valid}
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import io.circe.generic.JsonCodec
import com.timeout.healthchecks._
import cats.syntax.foldable._
import cats.instances.list._

import scala.collection.convert.DecorateAsScala
import scala.concurrent.{ExecutionContext, Future}

object HealthRoutes extends DecorateAsScala {

  @JsonCodec case class HealthResult(status: String, checks: Seq[String], failures: Seq[String])

  def status(s: Boolean) = if (s) "healthy" else "unhealthy"
  def statusCode(s: Boolean) = if (s) OK else InternalServerError

  def health(checks: HealthCheck*)(implicit ec: ExecutionContext) = path("health") {
    complete {
      Future.traverse(checks.toList){ c => c.run().map(c.severity -> _) }.map { r =>
        val healthy = r.forall(_._2.isValid)
        val fatal = r.exists { case (sev, res) => sev.fatal && !res.isValid }
        val checkNames = checks.map(_.name)
        val errorMessages = r.map(_._2).flatMap {
          case Invalid(errs) => errs.toList
          case Valid(_) => List()
        }
        statusCode(!fatal) -> HealthResult(status(healthy), checkNames, errorMessages)
      }
    }
  }

}
