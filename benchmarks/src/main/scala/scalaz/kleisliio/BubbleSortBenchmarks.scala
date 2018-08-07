package scalaz.zio

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

import scala.concurrent.duration.Duration
import scala.collection.immutable.Range

import scalaz.kleisliio.BenchmarkUtils.unsafeRun

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class BubbleSortBenchmarks {
  @Param(Array("1000"))
  var size: Int = _

  def createTestArray: Array[Int] = Range.inclusive(1, size).toArray.reverse
  def assertSorted(array: Array[Int]): Unit =
    if (!array.sorted.sameElements(array)) {
      throw new Exception("Array not correctly sorted")
    }

  @Benchmark
  def scalazBubbleSort() = {
    import scalaz.kleisliio.ScalazIOArray._

    unsafeRun(
      for {
        array <- IO.sync[Array[Int]](createTestArray)
        _ <- bubbleSort[Int](_ <= _)(array)
        _ <- IO.sync[Unit](assertSorted(array))
      } yield ()
    )
  }
  @Benchmark
  def catsBubbleSort() = {
    import scalaz.kleisliio.CatsIOArray._
    import cats.effect.IO

    (for {
      array <- IO(createTestArray)
      _ <- bubbleSort[Int](_ <= _)(array)
      _ <- IO(assertSorted(array))
    } yield ()).unsafeRunSync()
  }
  @Benchmark
  def monixBubbleSort() = {
    import scalaz.kleisliio.MonixIOArray._
    import monix.eval.Task
    import scalaz.kleisliio.BenchmarkUtils.monixScheduler

    (for {
      array <- Task.eval(createTestArray)
      _ <- bubbleSort[Int](_ <= _)(array)
      _ <- Task.eval(assertSorted(array))
    } yield ()).runSyncUnsafe(Duration.Inf)
  }
}
