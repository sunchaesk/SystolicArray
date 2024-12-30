

import chisel3._
import chiseltest._
import chiseltest.simulator.WriteVcdAnnotation
import org.scalatest.flatspec.AnyFlatSpec


class ProcElemSpec extends AnyFlatSpec with ChiselScalatestTester {
  "ProcessingElement" should "perform MAC operation" in {

//    val annotations = Seq(
//      VcsBackendAnnotation, // Use VCS as the backend
//      WriteVcdAnnotation,   // Enable VCD generation
//      VcsFlags(Seq("-timescale=1ns/1ps")) // Set the desired timescale (e.g., 1ns time unit, 1ps precision)
//    )

    test(new ProcessingElement()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      require(dut.input_bitwidth > 4)

      val in_horz = List(1, 2, 3, 0, 4, 0)
      val in_vert = List(4, 5, 6, 0, 4, 0)
      var accum = 0
      println("====ProcessingElementTester====")
      for ((h, v) <- in_horz zip in_vert){
        dut.io.out.expect(accum)

        accum += h * v

        dut.io.in_horz.poke(h.U)
        dut.io.in_vert.poke(v.U)

        val out_horz = dut.io.out_horz.peek()
        val out_vert = dut.io.out_vert.peek()
        val out = dut.io.out.peek()

        // println(s"--h=$h v=$v out_horz=$out_horz out_vert=$out_vert out=$out--")
        dut.clock.step()
      }
      dut.io.out.expect(accum)
    }
  }

}