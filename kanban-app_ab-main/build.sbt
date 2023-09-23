ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "Kanban App_Ab"
  )
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15"

//libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.32.0"

libraryDependencies += "io.github.cquiroz" %% "scala-java-time" % "2.5.0"

//libraryDependencies += "org.scalaj" %% "scalaj-time" % "0.7"


libraryDependencies += "org.scalafx" % "scalafx_3" % "19.0.0-R30"

val circeVersion = "0.14.4"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)