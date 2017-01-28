name := "cyclic-task-publisher"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.16"

val diDependencies = Seq(
  "com.google.inject" % "guice" % "4.1.0",
  "net.codingwell" % "scala-guice_2.11" % "4.1.0"
)

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test"
)

libraryDependencies ++= Seq(
  "net.jcazevedo" %% "moultingyaml" % "0.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "io.spray" %%  "spray-json" % "1.3.3"
) ++ diDependencies ++ akkaDependencies ++ testDependencies