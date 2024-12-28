
import chisel3._

class SystolicArray (val n: Int = 4, val input_bitwidth: Int = 8) extends Module {
  val io = IO(new Bundle {
                val data_horz = Input(Vec(n, UInt(input_bitwidth.W)))
                val data_vert = Input(Vec(n, UInt(input_bitwidth.W)))
                val out = Output(Vec(n * n, UInt((input_bitwidth * 2).W)))
              })

  val processing_elems = VecInit(Seq.fill(n * n) {Module(new ProcessingElement(input_bitwidth)).io})
  val h_wires = Wire(Vec((n-1)*n, UInt(input_bitwidth.W)))
  val v_wires = Wire(Vec(n*(n-1), UInt(input_bitwidth.W)))

  def gethidx(r: Int, c: Int): Int = r*(n-1) + c  // horizontal wire index
  def getvidx(r: Int, c: Int): Int = r*n + c      // vertical wire index

  for (col <- 0 until n) {
    for (row <- 0 until n) {
      val pidx = row * n + col

      io.out(pidx) := processing_elems(pidx).out

      // horizontal stuff
      if (col == 0) processing_elems(pidx).in_horz := io.data_horz(row)
      else          processing_elems(pidx).in_horz := h_wires(gethidx(row, col-1))
      // more horizontal wiring
      if (col < n-1) h_wires(gethidx(row, col)) := processing_elems(pidx).out_horz

      // vertical
      if (row == 0) processing_elems(pidx).in_vert := io.data_vert(col)
      else          processing_elems(pidx).in_vert := v_wires(getvidx(row-1, col))
      // more vertical wiring
      if (row < n-1) v_wires(getvidx(row, col)) := processing_elems(pidx).out_vert
    }
  }
}

import _root_.circt.stage.ChiselStage

object SystolicArrayDriver extends App {
  ChiselStage.emitSystemVerilogFile(
    new SystolicArray,
    Array("--target-dir", "generated/"),
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info"),
  )

  // print to shell
  val verilog_content = ChiselStage.emitSystemVerilog(
    new SystolicArray,
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
  )
  println(verilog_content)
}