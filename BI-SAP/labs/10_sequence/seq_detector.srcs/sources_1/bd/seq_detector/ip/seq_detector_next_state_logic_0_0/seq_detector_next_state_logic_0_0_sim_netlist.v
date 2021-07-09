// Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
// --------------------------------------------------------------------------------
// Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
// Date        : Thu Mar 26 18:58:30 2020
// Host        : THATSMEC804 running 64-bit major release  (build 9200)
// Command     : write_verilog -force -mode funcsim
//               c:/Users/kvetinac97/seq_detector/seq_detector.srcs/sources_1/bd/seq_detector/ip/seq_detector_next_state_logic_0_0/seq_detector_next_state_logic_0_0_sim_netlist.v
// Design      : seq_detector_next_state_logic_0_0
// Purpose     : This verilog netlist is a functional simulation representation of the design and should not be modified
//               or synthesized. This netlist cannot be used for SDF annotated simulation.
// Device      : xc7a12ticsg325-1L
// --------------------------------------------------------------------------------
`timescale 1 ps / 1 ps

(* CHECK_LICENSE_TYPE = "seq_detector_next_state_logic_0_0,next_state_logic,{}" *) (* downgradeipidentifiedwarnings = "yes" *) (* ip_definition_source = "module_ref" *) 
(* x_core_info = "next_state_logic,Vivado 2018.2" *) 
(* NotValidForBitStream *)
module seq_detector_next_state_logic_0_0
   (q0,
    q1,
    q2,
    sw,
    d0,
    d1,
    d2);
  input q0;
  input q1;
  input q2;
  input sw;
  output d0;
  output d1;
  output d2;

  wire d0;
  wire d1;
  wire d2;
  wire q0;
  wire q1;
  wire q2;
  wire sw;

  seq_detector_next_state_logic_0_0_next_state_logic U0
       (.d1(d1),
        .d2(d2),
        .q0(q0),
        .q1(q1),
        .q2(q2),
        .sw(sw));
  LUT4 #(
    .INIT(16'h20FF)) 
    d0_INST_0
       (.I0(q0),
        .I1(q1),
        .I2(q2),
        .I3(sw),
        .O(d0));
endmodule

(* ORIG_REF_NAME = "next_state_logic" *) 
module seq_detector_next_state_logic_0_0_next_state_logic
   (d1,
    d2,
    q2,
    q1,
    sw,
    q0);
  output d1;
  output d2;
  input q2;
  input q1;
  input sw;
  input q0;

  wire d1;
  wire d2;
  wire q0;
  wire q1;
  wire q2;
  wire sw;

  (* SOFT_HLUTNM = "soft_lutpair0" *) 
  LUT4 #(
    .INIT(16'hF440)) 
    d1__0
       (.I0(q2),
        .I1(q1),
        .I2(sw),
        .I3(q0),
        .O(d1));
  (* SOFT_HLUTNM = "soft_lutpair0" *) 
  LUT4 #(
    .INIT(16'h38B8)) 
    d2__0
       (.I0(q1),
        .I1(sw),
        .I2(q0),
        .I3(q2),
        .O(d2));
endmodule
`ifndef GLBL
`define GLBL
`timescale  1 ps / 1 ps

module glbl ();

    parameter ROC_WIDTH = 100000;
    parameter TOC_WIDTH = 0;

//--------   STARTUP Globals --------------
    wire GSR;
    wire GTS;
    wire GWE;
    wire PRLD;
    tri1 p_up_tmp;
    tri (weak1, strong0) PLL_LOCKG = p_up_tmp;

    wire PROGB_GLBL;
    wire CCLKO_GLBL;
    wire FCSBO_GLBL;
    wire [3:0] DO_GLBL;
    wire [3:0] DI_GLBL;
   
    reg GSR_int;
    reg GTS_int;
    reg PRLD_int;

//--------   JTAG Globals --------------
    wire JTAG_TDO_GLBL;
    wire JTAG_TCK_GLBL;
    wire JTAG_TDI_GLBL;
    wire JTAG_TMS_GLBL;
    wire JTAG_TRST_GLBL;

    reg JTAG_CAPTURE_GLBL;
    reg JTAG_RESET_GLBL;
    reg JTAG_SHIFT_GLBL;
    reg JTAG_UPDATE_GLBL;
    reg JTAG_RUNTEST_GLBL;

    reg JTAG_SEL1_GLBL = 0;
    reg JTAG_SEL2_GLBL = 0 ;
    reg JTAG_SEL3_GLBL = 0;
    reg JTAG_SEL4_GLBL = 0;

    reg JTAG_USER_TDO1_GLBL = 1'bz;
    reg JTAG_USER_TDO2_GLBL = 1'bz;
    reg JTAG_USER_TDO3_GLBL = 1'bz;
    reg JTAG_USER_TDO4_GLBL = 1'bz;

    assign (strong1, weak0) GSR = GSR_int;
    assign (strong1, weak0) GTS = GTS_int;
    assign (weak1, weak0) PRLD = PRLD_int;

    initial begin
	GSR_int = 1'b1;
	PRLD_int = 1'b1;
	#(ROC_WIDTH)
	GSR_int = 1'b0;
	PRLD_int = 1'b0;
    end

    initial begin
	GTS_int = 1'b1;
	#(TOC_WIDTH)
	GTS_int = 1'b0;
    end

endmodule
`endif
