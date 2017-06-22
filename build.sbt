name := "akka-http-healthchecks"
version := "1.3.0"
scalaVersion := "2.11.11"
organization := "com.timeout"
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")

val circeVersion = "0.8.0"
val akkaVersion = "2.4.19"
val akkaHttpVersion = "10.0.8"
val catsVersion = "0.9.0"
val scalaTestVersion = "3.0.3"
val akkaHttpCirceVersion = "1.16.1"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.11",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "org.typelevel" %% "cats" % catsVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    exclude("org.scala-lang.modules", "scala-xml_2.11")
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
