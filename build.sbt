name := "cyclic-task-publisher"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "net.jcazevedo" %% "moultingyaml" % "0.4.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided",
  "com.softwaremill.macwire" %% "util" % "2.2.5",
  "com.softwaremill.macwire" %% "proxy" % "2.2.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.scalaj" % "scalaj-http_2.12" % "2.3.0"
)