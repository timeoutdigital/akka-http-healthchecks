package com.timeout.healthchecks.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import de.heikoseeberger.akkahttpcirce.CirceSupport
import io.circe.generic.JsonCodec
import com.timeout.healthchecks._
import cats.syntax.foldable._
import cats.instances.list._

import scala.collection.convert.DecorateAsScala
import scala.concurrent.{ExecutionContext, Future}

object HealthRoutes extends CirceSupport with DecorateAsScala {

  @JsonCodec case class HealthResult(status: String, checks: Seq[String], failures: Seq[String])

  def status(s: Boolean) = if (s) "healthy" else "unhealthy"
  def statusCode(s: Boolean) = if (s) OK else InternalServerError

  def health(checks: HealthCheck*)(implicit ec: ExecutionContext) = path("health") {
    complete {
      Future.traverse(checks.toList)(_.run()).map { r =>
        val result = r.sequenceU_
        val healthy = result.isValid
        val checkNames = checks.map(_.name)
        val errorMessages = result.fold(_.map(identity).toList, _ => List())
        statusCode(healthy) -> HealthResult(status(healthy), checkNames, errorMessages)
      }
    }
  }

}
