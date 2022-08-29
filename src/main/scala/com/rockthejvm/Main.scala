package com.rockthejvm
object Main {
  def main(args: Array[String]): Unit = {
    println("Hello, World!")
    val portValue = Option(System.getProperty("port"))
    println("PORT value from argument is: "+portValue)
    val userValue = Option(System.getProperty("user"))
    println("User value from argument is: "+userValue)
  }
}
