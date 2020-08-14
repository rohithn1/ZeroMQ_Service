name := "ZMqTest"

version := "0.1"

scalaVersion := "2.12.11"

resolvers += Resolver.jcenterRepo

libraryDependencies += "ru.dgis" %% "reactive-zmq" % "0.4.0"

lazy val root = project.in(file(".")).settings(Compile / run / mainClass := Some("scala.ZMQServiceTester"))