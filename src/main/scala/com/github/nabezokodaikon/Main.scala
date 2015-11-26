package com.github.nabezokodaikon

import scala.concurrent._

import com.typesafe.scalalogging.LazyLogging

case class Sum(a: Int, b: Int) {
  def result(): Int = {
    Thread.sleep(3000)
    a + b
  }
}

object Main extends LazyLogging {

  def main(args: Array[String]) = {
    example2()
  }

  // def example3() = {
    // import scala.concurrent.ExecutionContext.Implicits.global

    // val a = Future[String] = Future {
    // }
  // }

  def example2() = {
    val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    def now() = new java.util.Date()
    (1 to 30).foreach { _ =>
      ec.execute(new Runnable {
        def run {
          Thread.sleep(3000)
          println(s"fuga! ${now}")
        }
      })
    }
  }

  def example1() = {
    val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    def now() = new java.util.Date()
    println(now)
    ec.execute(new Runnable {
      def run: Unit = {
        Thread.sleep(3000)
        println(s"fuga! ${now}")
      }
    })

    println(now)
  }
}
