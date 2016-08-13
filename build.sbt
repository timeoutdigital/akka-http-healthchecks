name := "health-check"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.11.8"
organization := "com.timeout"

// TODO: add publishing config
//private val timeOutNexus = "http://nexus.repo.timeout.com/nexus/content/repositories/"
//val timeOutReleases = "TimeOut Releases" at timeOutNexus + "releases"
//
//publishTo := Some(timeOutReleases)

val circeVersion = "0.5.0-M2"
val akkaVersion = "2.4.9-RC2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
  "org.typelevel" %% "cats" % "0.6.1",
  "de.heikoseeberger" %% "akka-http-circe" % "1.8.0",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)