// Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
// --------------------------------------------------------------------------------
// Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
// Date        : Tue Mar  3 08:20:00 2020
// Host        : PC-1042-103 running 64-bit major release  (build 9200)
// Command     : write_verilog -force -mode synth_stub
//               c:/testsap/segment7/segment7.srcs/sources_1/bd/design_1/ip/design_1_and_1_0/design_1_and_1_0_stub.v
// Design      : design_1_and_1_0
// Purpose     : Stub declaration of top-level module interface
// Device      : xc7a35tcpg236-1
// --------------------------------------------------------------------------------

// This empty module with port declaration file causes synthesis tools to infer a black box for IP.
// The synthesis directives are for Synopsys Synplify support to prevent IO buffer insertion.
// Please paste the declaration into a Verilog source file or add the file as an additional source.
(* x_core_info = "and_gate,Vivado 2018.2.1" *)
module design_1_and_1_0(I0, I1, I2, O)
/* synthesis syn_black_box black_box_pad_pin="I0,I1,I2,O" */;
  input I0;
  input I1;
  input I2;
  output O;
endmodule
