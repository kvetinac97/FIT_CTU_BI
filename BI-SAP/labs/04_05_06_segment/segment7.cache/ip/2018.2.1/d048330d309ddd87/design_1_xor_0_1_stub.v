// Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
// --------------------------------------------------------------------------------
// Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
// Date        : Tue Mar  3 08:21:30 2020
// Host        : PC-1042-103 running 64-bit major release  (build 9200)
// Command     : write_verilog -force -mode synth_stub -rename_top decalper_eb_ot_sdeen_pot_pi_dehcac_xnilix -prefix
//               decalper_eb_ot_sdeen_pot_pi_dehcac_xnilix_ design_1_xor_0_1_stub.v
// Design      : design_1_xor_0_1
// Purpose     : Stub declaration of top-level module interface
// Device      : xc7a35tcpg236-1
// --------------------------------------------------------------------------------

// This empty module with port declaration file causes synthesis tools to infer a black box for IP.
// The synthesis directives are for Synopsys Synplify support to prevent IO buffer insertion.
// Please paste the declaration into a Verilog source file or add the file as an additional source.
(* x_core_info = "xor_gate,Vivado 2018.2.1" *)
module decalper_eb_ot_sdeen_pot_pi_dehcac_xnilix(I0, I1, O)
/* synthesis syn_black_box black_box_pad_pin="I0,I1,O" */;
  input I0;
  input I1;
  output O;
endmodule
