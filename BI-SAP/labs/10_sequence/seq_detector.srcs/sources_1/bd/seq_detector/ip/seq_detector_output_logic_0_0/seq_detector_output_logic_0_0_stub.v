// Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
// --------------------------------------------------------------------------------
// Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
// Date        : Thu Mar 26 18:59:26 2020
// Host        : THATSMEC804 running 64-bit major release  (build 9200)
// Command     : write_verilog -force -mode synth_stub
//               c:/Users/kvetinac97/seq_detector/seq_detector.srcs/sources_1/bd/seq_detector/ip/seq_detector_output_logic_0_0/seq_detector_output_logic_0_0_stub.v
// Design      : seq_detector_output_logic_0_0
// Purpose     : Stub declaration of top-level module interface
// Device      : xc7a12ticsg325-1L
// --------------------------------------------------------------------------------

// This empty module with port declaration file causes synthesis tools to infer a black box for IP.
// The synthesis directives are for Synopsys Synplify support to prevent IO buffer insertion.
// Please paste the declaration into a Verilog source file or add the file as an additional source.
(* x_core_info = "output_logic,Vivado 2018.2" *)
module seq_detector_output_logic_0_0(q0, q1, q2, sw, y)
/* synthesis syn_black_box black_box_pad_pin="q0,q1,q2,sw,y" */;
  input q0;
  input q1;
  input q2;
  input sw;
  output y;
endmodule
