
name := "akka-http-healthchecks"
scalaVersion := "2.13.3"
organization := "com.timeout"
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
releaseCrossBuild := true
//ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

val circeVersion = "0.13.0"
val akkaVersion = "2.6.6"
val akkaHttpVersion = "10.1.12"
val catsVersion = "2.1.1"
val scalaTestVersion = "3.2.0"
val akkaHttpCirceVersion = "1.33.0"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "8.0.23",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream"% akkaVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

resolvers ++= Seq(
  "MySql driver" at "https://nexus.timeout.com/content/repositories",
)
lazy val root = (project in file("."))
  .settings(BintrayPlugin.bintrayPublishSettings: _*)
  .settings(Seq(
    bintrayOrganization := Some("timeoutdigital"),
    bintrayRepository := "releases"
  ))
