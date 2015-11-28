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

  def example7() = {
    val fs: Seq[Future[Int]] = Seq(
      Future { Thread.sleep(1000); 1 },
      Future { Thread.sleep(1000); 3 },
      Future { Thread.sleep(1000); 5 },
      Future { Thread.sleep(1000); 7 }
    )

    val f = Future.fold(fs)(10) { (total, v) => total + v }
    f.onSuccess {
      case r: Int => println(r)
    }

    Await.ready(f, Duration.Inf)
  }

  def example8() = {
    val fs: Seq[Future[Int]] = Seq(
      Future { Thread.sleep(1000); 1 },
      Future { Thread.sleep(1000); 3 },
      Future { Thread.sleep(1000); 5 },
      Future { Thread.sleep(1000); 7 }
    )

    val f: Future[Int] = Future.reduce(fs) { (total, v) => total + v }
    f.onSuccess {
      case r: Int => println(r)
    }

    Await.ready(f, Duration.Inf)
  }

  def example9() = {
    val fs1: Future[List[Int]] = Future.traverse((1 to 5).toList) { i =>
      Future {
        Thread.sleep(i * 100)
        i
      }
    }

    val fs2: Future[List[Int]] = Future.traverse((6 to 10).toList) { i =>
      Future {
        Thread.sleep(i * 100)
        i
      }
    }

    val f: Future[List[Int]] = Future.fold(List(fs1, fs2))(List(10)) { (total, v) => v ++ total }
    f.onSuccess {
      case r: List[Int] => println(r.sum)
    }

    Await.ready(f, Duration.Inf)
  }

}
