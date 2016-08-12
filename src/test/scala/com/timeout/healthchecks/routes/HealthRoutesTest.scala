package com.timeout.healthchecks.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import cats.syntax.all._
import com.timeout.healthchecks._

class HealthRoutesTest extends FunSpec with ScalatestRouteTest with Matchers with OneInstancePerTest {

  describe("Health route") {
    it ("should return correct response for healthy system") {
      val ok1 = healthCheck("test1", () => ().validNel[String])
      val ok2 = healthCheck("test2", () => ().validNel[String])

      Get("/health") ~> HealthRoutes.health(ok1, ok2) ~> check {
        status shouldEqual OK
        responseAs[String] shouldEqual """{"status":"OK","checks":["test1","test2"],"failures":[]}"""
      }
    }

    it ("should return correct response for unhealthy system") {
      val ok = healthCheck("test1", () => ().validNel[String])
      val failed1 = healthCheck("test2", () => "failed".invalidNel[Unit])
      val failed2 = healthCheck("test3", () => { throw new Exception("exception") })

      Get("/health") ~> HealthRoutes.health(ok, failed1, failed2) ~> check {
        status shouldEqual InternalServerError
        responseAs[String] shouldEqual """{"status":"KO","checks":["test1","test2","test3"],"failures":["test2 failed: failed","test3 failed: exception"]}"""
      }
    }
  }

}
