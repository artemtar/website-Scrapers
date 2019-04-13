name := "ScalaScrap"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5-SNAPSHOT"
libraryDependencies += "com.ning" % "async-http-client" % "1.9.40"
libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"