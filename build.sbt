name := "com.comsystoreply.lab.akka.streams"

version := "0.1"

scalaVersion := "2.12.8"

lazy val akkaHttpVersion = "10.0.11"
lazy val akkaVersion    = "2.5.21"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.comsystoreply",
      scalaVersion    := "2.12.4"
    )),
    name := "twitter-stream-to-kafka-example",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.typesafe"      %  "config"               % "1.3.2",
      "io.spray"          %%  "spray-json"          % "1.3.5",

      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.1"         % Test
    )
  )
