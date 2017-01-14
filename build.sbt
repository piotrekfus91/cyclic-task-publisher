name := "cyclic-task-publisher"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "net.jcazevedo" %% "moultingyaml" % "0.4.0",
  "com.google.inject" % "guice" % "4.1.0",
  "net.codingwell" % "scala-guice_2.11" % "4.1.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "io.spray" %%  "spray-json" % "1.3.3",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.16",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test"
)