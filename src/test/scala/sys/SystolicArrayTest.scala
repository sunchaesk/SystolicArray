
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Random

class MatrixUtils {
  private val random = new Random()

  def generateRandomSquareMatrix(n: Int = 2, maxval: Int = 10): Seq[Seq[Int]] = {
    Seq.fill(n, n)(random.nextInt(maxval))
  }

  def matrixMultiply(a: Seq[Seq[Int]], b: Seq[Seq[Int]]): Seq[Seq[Int]] = {
    require(a.length == a.head.length, "a Matrix must be square")
    require(b.length == b.head.length, "b Matrix must be square")
    require(a.length == b.length, "a & b Matrices must have same dimensions")

    for (r <- a) yield {
      for (c <- b.transpose) yield r zip c map Function.tupled(_*_)reduceLeft (_+_)
    }
  }

  def printMatrix(m: Seq[Seq[Int]]): Unit = {
    m.foreach{r => r.foreach{v => print(f"$v%4d")} ; print(" ;")}
    println()
  }
}

class SystolicArraySpec extends AnyFlatSpec with ChiselScalatestTester {
  "SystolicArray" should "perform square MM operation" in {
    test(new SystolicArray()) { dut =>
      require(dut.input_bitwidth > 4)


    }
  }
}