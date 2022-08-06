scalaVersion := "2.13.8"
name := "hello-world"
organization := "ch.epfl.scala"
version := "1.0"

lazy val printerTask = taskKey[Unit]("Simple custom task")
printerTask := {
    CustomTaskPrinter.print()
}

addCommandAlias("ci", ";clean; compile;test; assembly;")