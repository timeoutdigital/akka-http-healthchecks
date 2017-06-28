name := "akka-http-healthchecks"
scalaVersion := "2.11.11"
organization := "com.timeout"
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")
releaseCrossBuild := true
crossScalaVersions := Seq("2.11.11", "2.12.2")

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

val circeVersion = "0.8.0"
val akkaVersion = "2.4.19"
val akkaHttpVersion = "10.0.9"
val catsVersion = "0.9.0"
val scalaTestVersion = "3.0.3"
val akkaHttpCirceVersion = "1.16.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "org.typelevel" %% "cats" % catsVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
)

lazy val root = (project in file("."))
  .settings(BintrayPlugin.bintrayPublishSettings: _*)
  .settings(Seq(
    bintrayOrganization := Some("timeoutdigital"),
    bintrayRepository := "releases"
  ))
