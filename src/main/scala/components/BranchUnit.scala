package components

import chisel3._
import chisel3.util._

class BranchUnit extends Module {
  val io = IO(new Bundle {
    val branch = Input(Bool())
    val funct3 = Input(UInt(3.W))
    val rd1 = Input(UInt(64.W))
    val rd2 = Input(UInt(64.W))

    val taken = Output(Bool())
  })

  io.taken := DontCare

  val check = Wire(Bool())
  check := DontCare

  switch(io.funct3) {
    is(0.U) { check := (io.rd1 === io.rd2) } // beq
    is(1.U) { check := (io.rd1 =/= io.rd2) } // bne
    is(4.U) { check := (io.rd1.asSInt < io.rd2.asSInt) } // blt
    is(5.U) { check := (io.rd1.asSInt >= io.rd2.asSInt) } // bge
    is(6.U) { check := (io.rd1 < io.rd2) } // bltu
    is(7.U) { check := (io.rd1 >= io.rd2) } // bgeu
  }

  io.taken := check & io.branch

}