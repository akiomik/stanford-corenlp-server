import sbt._
import sbt.Keys._

object StanfordCoreNLPServerBuild extends Build {

  lazy val stanfordCoreNLPServer = Project(
    id = "stanford-corenlp-server",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "stanford-corenlp-server",
      organization := "com.github.akiomik",
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.11.7",
      resolvers += "twttr" at "https://maven.twttr.com/",
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.3.0",
        "com.github.finagle" %% "finch-core" % "0.9.1",
        "com.github.finagle" %% "finch-circe" % "0.9.1",
        "io.circe" %% "circe-generic" % "0.2.0",
        "org.spire-math" %% "jawn-argonaut" % "0.8.3",
        "com.twitter" %% "twitter-server" % "1.15.0"
      )
    )
  )
}
