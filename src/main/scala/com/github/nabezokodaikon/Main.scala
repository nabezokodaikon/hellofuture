package com.github.nabezokodaikon

import com.typesafe.scalalogging.LazyLogging

case class Sum(a: Int, b: Int) {
  def result(): Int = {
    Thread.sleep(3000)
    a + b
  }
}

object Main extends LazyLogging {

  def main(args: Array[String]) = {
    Example1.example3()
  }

}
