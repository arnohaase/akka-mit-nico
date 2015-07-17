name := """akka-hello"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"           % "2.4-M2",
  "com.typesafe.akka" %% "akka-cluster"         % "2.4-M2",
  "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4-M2",
  "com.typesafe.akka" %% "akka-cluster-tools"   % "2.4-M2",
  "com.typesafe.akka" %% "akka-remote"          % "2.4-M2",
  "com.typesafe.akka" %% "akka-contrib"         % "2.4-M2",
  "com.typesafe.akka" %% "akka-testkit" % "2.4-M2",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
