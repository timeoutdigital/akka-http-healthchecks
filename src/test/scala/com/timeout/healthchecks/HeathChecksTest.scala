package com.timeout.healthchecks

import org.scalatest.{FunSpec, Matchers}
import cats.syntax.all._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.time.{Millis, Second, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class HeathChecksTest extends FunSpec with ScalaFutures with Matchers with TableDrivenPropertyChecks {

  override implicit def patienceConfig: PatienceConfig = PatienceConfig(Span(100, Millis), Span(1, Second))

  describe("healthCheck") {
    it ("should add check name to error message") {
      val result = healthCheck("check1", () => "error".invalidNel).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: error".invalidNel
      }
    }

    it ("should handle exceptions as failed checks") {
      val result = healthCheck("check1", () => throw new RuntimeException("exception")).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: exception".invalidNel
      }
    }
  }

  describe("asyncHealthCheck") {
    it ("should add check name to error message") {
      val result = asyncHealthCheck("check1", () => Future.successful("error".invalidNel)).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: error".invalidNel
      }
    }

    it ("should handle exceptions as failed checks") {
      val result = asyncHealthCheck("check1", () => Future(throw new Exception("exception"))).run()
      whenReady(result) { r =>
        r shouldEqual "check1 failed: exception".invalidNel
      }
    }

  }

}
