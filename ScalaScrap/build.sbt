name := "ScalaScrap"

version := "0.1"

scalaVersion := "2.12.0"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.22"
libraryDependencies += "com.ning" % "async-http-client" % "1.9.40"
libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.1.0"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.5"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9"
libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1"
libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-tcp" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-http" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-streams" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-circe" % "5.5.3",
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % "5.5.3" % "test",
  "org.apache.logging.log4j" % "log4j-api" % "2.9.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.9.1"
)