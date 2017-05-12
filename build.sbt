name := "movie-ticket-system-design-excercise"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.4.17",
	"com.typesafe.akka" %% "akka-testkit" % "2.4.17",

	"com.typesafe.akka" %% "akka-http-core" % "10.0.6",
	"com.typesafe.akka" %% "akka-http-testkit" % "10.0.6",
	"com.typesafe.akka" %% "akka-http-spray-json" % "10.0.6",

	"org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

mainClass in assembly := Some("Main")
