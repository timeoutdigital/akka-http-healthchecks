package com.timeout.healthchecks.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import cats.syntax.all._
import com.timeout.healthchecks.HealthChecks.Severity.NonFatal
import com.timeout.healthchecks._

class HealthRoutesTest extends FunSpec with ScalatestRouteTest with Matchers with OneInstancePerTest {

  describe("Health route") {
    it ("should return correct response for healthy system") {
      val ok1 = healthCheck("test1")(().validNel[String])
      val ok2 = healthCheck("test2")(().validNel[String])

      Get("/health") ~> HealthRoutes.health(ok1, ok2) ~> check {
        status shouldEqual OK
        responseAs[String] shouldEqual """{"status":"healthy","checks":["test1","test2"],"failures":[]}"""
      }
    }

    it ("should return correct response for unhealthy system with a fatal error") {
      val ok = healthCheck("test1")(().validNel[String])
      val failed1 = healthCheck("test2")("failed".invalidNel[Unit])
      val failed2 = healthCheck("test3")(throw new Exception("exception") )

      Get("/health") ~> HealthRoutes.health(ok, failed1, failed2) ~> check {
        status shouldEqual InternalServerError
        responseAs[String] shouldEqual """{"status":"unhealthy","checks":["test1","test2","test3"],"failures":["test2 failed: failed","test3 failed: exception"]}"""
      }
    }

    it ("should return correct response for unhealthy system with a nonfatal error") {
      val ok = healthCheck("test1")(().validNel[String])
      val failed = healthCheck("test2", NonFatal)("failed".invalidNel[Unit])

      Get("/health") ~> HealthRoutes.health(ok, failed) ~> check {
        status shouldEqual OK
        responseAs[String] shouldEqual """{"status":"unhealthy","checks":["test1","test2"],"failures":["test2 failed: failed"]}"""
      }
    }
  }

}
