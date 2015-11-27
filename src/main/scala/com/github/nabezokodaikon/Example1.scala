package com.github.nabezokodaikon

import java.lang.Throwable

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Await, Future }
import scala.util.{ Success, Failure, Try }

object Example1 {

  def example3() = {

    val f: Future[String] = Future {
      Thread.sleep(1000)
      // throw new Exception("Error!")
      "hello"
    }

    f.onSuccess {
      case r: String => println(r)
    }

    f.onFailure {
      case t: Throwable => println(t.getMessage())
    }

    f.onComplete {
      case Success(r) => println(r)
      case Failure(t) => println(t.getMessage)
    }
  }

  def example2() = {

    val f: Future[String] = Future {
      Thread.sleep(1000)
      throw new Exception("Error!")
      "Hello!"
    }

    Await.ready(f, Duration.Inf)
    val r = f.value.get match {
      case Success(msg) => msg
      case Failure(ex)  => ex.toString
    }
    println(r)
  }

  def example1() = {

    val f: Future[String] = Future {
      Thread.sleep(1000)
      "Hello!"
    }

    println(f.isCompleted)
    val r1: Option[Try[String]] = f.value
    println(r1)

    Thread.sleep(2000)

    println(f.isCompleted)
    val r2: Option[Try[String]] = f.value
    println(r2.get.get)

  }
}
