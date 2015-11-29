package com.github.nabezokodaikon

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure, Try }

object Example3 {

  def example1() = {
    val f: Future[String] = Future("hello")
    val ff: Future[Int] = f.map(_.size)
    ff.onSuccess {
      case r: Int => println(r)
    }
    Await.ready(ff, Duration.Inf)
  }

  def example2() = {
    trait X
    trait A
    trait B extends A
    case class Foo(i: Int) extends X with B

    val f: Future[Foo] = Future { Foo(1) }
    val fx: Future[X] = f
    val fb: Future[B] = fx.mapTo[B]
    val fa: Future[A] = fb
  }

  def example3() = {
    val f: Future[Int] = Future[Int] { throw new Exception("Error"); 1 }
    val fe = f.failed
    fe.onSuccess {
      case e => println(e.getMessage)
    }
    Await.ready(fe, Duration.Inf)
  }

  def example4() = {
    val f1: Future[Int] = Future { Thread.sleep(2000); 1 }
    val f2: Future[Int] = Future { Thread.sleep(1000); 2 }

    val f: Future[Int] = f1.flatMap { n1: Int =>
      val ff: Future[String] = f2.map { n2: Int =>
        val sum = n1 + n2
        s"Hello$sum"
      }

      // ff.onSuccess {
      // case r => println(r)
      // }

      // Await.ready(ff, Duration.Inf)
      ff
    }.map { str =>
      str.size
    }

    Await.ready(f, Duration.Inf)

    println(f.value.get)
  }

  def example5() {
    val f: Future[Int] = Future { 200 / 1 }
    val ff: Future[Int] = f.transform(
      { n => n * 2 },
      { t => throw new Exception("Error!") }
    )

    ff.onComplete {
      case Success(r) => println(r)
      case Failure(r) => println(r)
    }
    Await.ready(ff, Duration.Inf)
  }

  def example6() {
    val f: Future[Int] = Future { 6 }
    val ff: Future[Int] = f.filter(_ % 2 == 0).map(_ * 2)
    ff.onComplete {
      case Success(r) => println(r)
      case Failure(t) => println(t.getMessage)
    }
    Await.ready(ff, Duration.Inf)
  }

  def example7() {
    val f: Future[Int] = Future { 6 }
    val ff = f.collect {
      case v: Int if (v % 2 == 0) => println(s"Hello $v")
    }
    Await.ready(ff, Duration.Inf)
  }

  def example8() {
    val f: Future[List[Int]] = Future.traverse((1 to 10).toList) { i =>
      Future {
        if (i == 5 || i == 7) {
          throw new Exception(s"Error $i")
        } else {
          i
        }
      } recover {
        case t =>
          println(t.getMessage)
          -1
      }
    }

    f.onSuccess {
      case r: List[Int] =>
        val grouped = r.groupBy(v => v != -1)
        def count(key: Boolean): Int = grouped.get(key).map(_.size).getOrElse(0)
        println(s"success: ${count(true)}, fail: ${count(false)}")
    }

    Await.ready(f, Duration.Inf)
  }

  def example9() {
    val f: Future[List[Int]] = Future.traverse((1 to 10).toList) { i =>
      Future {
        if (i == 5 || i == 7) {
          throw new Exception(s"Error $i")
        } else {
          i
        }
      } fallbackTo {
        Future.successful(-1)
      }
    }

    f.onSuccess {
      case r: List[Int] =>
        val grouped = r.groupBy(v => v != -1)
        def count(key: Boolean): Int = grouped.get(key).map(_.size).getOrElse(0)
        println(s"success: ${count(true)}, fail: ${count(false)}")
    }

    Await.ready(f, Duration.Inf)
  }
}
