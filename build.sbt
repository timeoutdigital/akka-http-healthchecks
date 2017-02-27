name := "akka-http-healthchecks"
version := "1.2.0-SNAPSHOT"
scalaVersion := "2.11.8"
organization := "com.timeout"
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")

val circeVersion = "0.5.2"
val akkaVersion = "2.4.17"
val akkaHttpVersion = "10.0.4"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
  "org.typelevel" %% "cats" % "0.7.2",
  "de.heikoseeberger" %% "akka-http-circe" % "1.10.1"
    exclude("com.typesafe.akka", "akka-http-experimental_2.11"),
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    exclude("org.scala-lang.modules", "scala-xml_2.11")
)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)

lazy val root = (project in file("."))
  .settings(BintrayPlugin.bintrayPublishSettings: _*)
  .settings(Seq(
    bintrayOrganization := Some("timeoutdigital"),
    bintrayRepository := "releases"
  ))