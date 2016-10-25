
name := "akka-http-healthchecks"
scalaVersion := "2.11.8"
organization := "com.timeout"

val circeVersion = "0.5.2"
val akkaVersion = "2.4.11"

enablePlugins(GitVersioning, BintrayPlugin)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test",
  "org.typelevel" %% "cats" % "0.7.2",
  "de.heikoseeberger" %% "akka-http-circe" % "1.10.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)

bintrayOrganization := Some("timeoutdigital")
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

git.useGitDescribe := true