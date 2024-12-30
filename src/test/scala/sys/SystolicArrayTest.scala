
import chisel3._
import chiseltest._
import chiseltest.simulator.WriteVcdAnnotation
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

//    val annotations = Seq(
//      VcsBackendAnnotation, // Use VCS as the backend
//      WriteVcdAnnotation,   // Enable VCD generation
//      VcsFlags(Seq("-timescale=1ns/1ps")) // Set the desired timescale (e.g., 1ns time unit, 1ps precision)
//    )

    test(new SystolicArray(4, 8)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      require(dut.input_bitwidth > 4)

      val m_utils = new MatrixUtils()

      val size = 2
      val maxVal = 10

      val matA = m_utils.generateRandomSquareMatrix(size, maxVal)
      val matB = m_utils.generateRandomSquareMatrix(size, maxVal)

      val matC = m_utils.matrixMultiply(matA, matB)
      val matC_flatten = matC.flatten

      println("========== A")
      m_utils.printMatrix(matA)
      println("========== B")
      m_utils.printMatrix(matB)
      println("========== C")
      m_utils.printMatrix(matC)
      println("==========")

      // now run the chiseltest stuff
      for (i <- 0 until size) {
        for (j <- 0 until size) {
          println(dut.io.data_horz, i * size + j)
          println(matA.length)
          println(matB.length)
          dut.io.data_horz(i * size + j).poke(matA(i)(j).U)
          dut.io.data_vert(i * size + j).poke(matB(i)(j).U)
        }
      }

      dut.clock.step()

      val value = dut.io.data_vert(1).peek().litValue
      println("value!:", value)

      dut.clock.step(10)

      val ret = for (k <- 0 until size * size) yield {
        val out = dut.io.out(k).peek().litValue
        out.toInt
      }
      println("output2:", ret)
    }
  }
}