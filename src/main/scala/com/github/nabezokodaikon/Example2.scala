package com.github.nabezokodaikon

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global

object Example2 {

  def example1() = {
    val f: Future[Int] = Future.successful(777)
    println(f.value.get.get)
  }

  def example2() = {
    val f: Future[String] = Future.fromTry(
      Try {
        throw new Exception("Error!")
      }
    )

    println(f.value.get)
  }

  def example3() = {
    val f: Future[List[Int]] = Future.traverse((1 to 12).toList) { i =>
      Future[Int] {
        println(s"${i} start")
        Thread.sleep(1000)
        println(s"${i} end")
        i
      }
    }

    f.onSuccess {
      case r: List[Int] => println(r.mkString(","))
    }

    Await.ready(f, Duration.Inf)
  }

  def example4() = {

    val fs: List[Future[Int]] = (1 to 12).toList.map { i =>
      Future {
        println(i)
        Thread.sleep(1000)
        i
      }
    }

    val f: Future[List[Int]] = Future.sequence(fs)
    f.onSuccess {
      case r: List[Int] => println(r.mkString)
    }

    Await.ready(f, Duration.Inf)
  }

  def example5() = {
    val fs: Seq[Future[Int]] = Seq(
      Future {
        Thread.sleep(10)
        throw new Exception("Error")
      },
      Future {
        Thread.sleep(500)
        5
      }
    )

    val f = Future.firstCompletedOf(fs)
    f.onSuccess {
      case r: Int => println(r)
    }
    f.onFailure {
      case t: Throwable => println(t.getMessage)
    }

    Await.ready(f, Duration.Inf)
  }

  def example6() = {
    val fs: Seq[Future[Int]] = Seq(
      Future { Thread.sleep(3000); 8 },
      Future { Thread.sleep(2000); 2 },
      Future { Thread.sleep(1000); 6 },
      Future { Thread.sleep(500); 4 }
    )

    val f: Future[Option[Int]] = Future.find(fs) { _ % 2 == 1 }
    f.onSuccess {
      case Some(r) => println(r)
      case None    => println("Error!")
    }

    Await.ready(f, Duration.Inf)
  }

}
