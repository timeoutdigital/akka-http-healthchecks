package com.timeout.healthchecks

import cats.syntax.all._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.time.{Millis, Second, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HeathChecksTest extends AnyFunSpec with ScalaFutures with Matchers with TableDrivenPropertyChecks {

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(100, Millis), Span(1, Second))

  describe("healthCheck") {
    it ("should add check name to error message") {
      val result = healthCheck("check1")("error".invalidNel).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: error".invalidNel
      }
    }

    it ("should handle exceptions as failed checks") {
      val result = healthCheck("check1")(throw new RuntimeException("exception")).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: exception".invalidNel
      }
    }
  }

  describe("asyncHealthCheck") {
    it ("should add check name to error message") {
      val result = asyncHealthCheck("check1")(Future.successful("error".invalidNel)).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: error".invalidNel
      }
    }

    it ("should handle exceptions as failed checks") {
      val result = asyncHealthCheck("check1") {
        Future(throw new Exception("exception"))
      }.run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: exception".invalidNel
      }
    }
  }

  describe("Ping check") {

    it ("should return ok for valid ping") {
      whenReady(HealthChecks.ping("google.com", 80).run()) { r =>
        r shouldEqual ().validNel[String]
      }
    }

    it ("should return not ok for invalid host") {
      whenReady(HealthChecks.ping("whatever", 80).run()) { r =>
        r shouldEqual "Ping whatever:80 failed: whatever".invalidNel[Unit]
      }
    }

    it ("should return not ok for invalid port") {
      whenReady(HealthChecks.ping("google.com", 8890).run()) { r =>
        r.isValid shouldEqual false
      }
    }

    it ("should return ok for valid url ping") {
      whenReady(HealthChecks.pingUrl("https://api.tableapp.com/partner/v1/restaurant-list").run()) { r =>
        r shouldEqual ().validNel[String]
      }
    }

    it ("should return ok for valid url ping without port") {
      whenReady(HealthChecks.pingUrl("http://www.google.com").run()) { r =>
        r shouldEqual ().validNel[String]
      }
    }

    it ("should return failure for invalid url") {
      whenReady(HealthChecks.pingUrl("whatever").run()) { r =>
        r.isValid shouldEqual false
      }
    }

    it ("should return failure for invalid host url") {
      whenReady(HealthChecks.pingUrl("http://whatever").run()) { r =>
        r.isValid shouldEqual false
      }
    }
  }



}
