import java.util.UUID
object CustomTaskPrinter {
  def print() = {
    println("Rock the SBT custom task is here....")
  }
}

object StringTask {
  def strTask(): String = {
    UUID.randomUUID().toString()
  }
}