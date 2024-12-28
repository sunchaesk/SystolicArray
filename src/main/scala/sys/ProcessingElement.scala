
import chisel3._

class ProcessingElement (val input_bitwidth: Int = 8) extends Module {
  val io = IO(new Bundle {
                val in_horz = Input(UInt(input_bitwidth.W))
                val in_vert = Input(UInt(input_bitwidth.W))
                val out_horz = Output(UInt((input_bitwidth * 2).W))
                val out_vert = Output(UInt((input_bitwidth * 2).W))
                val out = Output(UInt((input_bitwidth * 2).W))
              })

  val res = RegInit(0.U((input_bitwidth * 2).W))
  val hreg = RegInit(0.U((input_bitwidth * 2).W))
  val vreg = RegInit(0.U((input_bitwidth * 2).W))

  res := res + (io.in_horz * io.in_vert)

  // delay the inputs by one cycle to the output by storing
  // into a register
  io.out_horz := hreg
  io.out_vert := vreg
  io.out := res

  }


import _root_.circt.stage.ChiselStage

object ProcessingElementDriver extends App {
  // val verilog_content = ChiselStage.emitSystemVerilogFile(
  val verilog_content = ChiselStage.emitSystemVerilog(
    new ProcessingElement,
    firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
  )
  println(verilog_content)
}
