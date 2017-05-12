name := "movie-ticket-system-design-excercise"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.3.9",
	"com.typesafe.akka" %% "akka-testkit" % "2.3.9",
	"org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

mainClass in assembly := Some("Main")
