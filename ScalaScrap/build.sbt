name := "ScalaWeb"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.5-SNAPSHOT"


libraryDependencies += "com.ning" % "async-http-client" % "1.9.40"

libraryDependencies += "org.jsoup" % "jsoup" % "1.8.3"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5")

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
