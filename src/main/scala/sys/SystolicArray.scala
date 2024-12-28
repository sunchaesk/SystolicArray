
import chisel3._

class SystolicMatMul (val n: Int = 4, val input_bitwidth: Int = 8) extends Module {
  val io = IO(new Bundle {
                val data_horz = Input(Vec(n, UInt(input_bitwidth.W)))
                val data_vert = Input(Vec(n, UInt(input_bitwidth.W)))
                val out = Output(Vec(n * n, UInt((input_bitwidth).W)))
              })
}
