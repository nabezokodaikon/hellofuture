package com.github.nabezokodaikon

import scala.concurrent._

object Example0 {

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

  def example3() = {
    import scala.concurrent.ExecutionContext.Implicits.global

    val a: Future[String] = Future {
      Thread.sleep(3000)
      "fuga"
    }

    a.foreach(println)
  }

  def example4() = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val p: Promise[String] = Promise[String]
    val a: Future[String] = p.future
    a.foreach(println)
    scala.concurrent.ExecutionContext.Implicits.global.execute(
      new Runnable {
        def run = {
          Thread.sleep(3000)
          p.success("fuga")
        }
      }
    )
  }
}
