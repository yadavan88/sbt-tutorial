scalaVersion := "2.13.8"
name := "hello-world"
organization := "ch.epfl.scala"
version := "1.0"

lazy val printerTask = taskKey[Unit]("Simple custom task")
printerTask := {
    val uuid = uuidStringTask.value
    println("Generated uuid from task:"+uuid)
    val uuidSetting = uuidStringSetting.value
    println("Generated uuid from setting:"+uuidSetting)
    CustomTaskPrinter.print()
}
lazy val uuidStringTask = taskKey[String]("Generate string uuid task")
uuidStringTask := {
    val uuid = StringTask.strTask()
    println("Evaluating task...."+uuid)
    uuid
}

lazy val uuidStringSetting = settingKey[String]("Generate string uuid task")
uuidStringSetting := {
    val uuid = StringTask.strTask()
    println("Evaluating settings... "+uuid)
    uuid
}


addCommandAlias("ci", ";clean; compile;test; assembly;")