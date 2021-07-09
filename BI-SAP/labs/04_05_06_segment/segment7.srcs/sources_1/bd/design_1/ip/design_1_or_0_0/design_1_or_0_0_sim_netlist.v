// Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
// --------------------------------------------------------------------------------
// Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
// Date        : Tue Mar  3 08:20:44 2020
// Host        : PC-1042-103 running 64-bit major release  (build 9200)
// Command     : write_verilog -force -mode funcsim -rename_top design_1_or_0_0 -prefix
//               design_1_or_0_0_ design_1_or_0_1_sim_netlist.v
// Design      : design_1_or_0_1
// Purpose     : This verilog netlist is a functional simulation representation of the design and should not be modified
//               or synthesized. This netlist cannot be used for SDF annotated simulation.
// Device      : xc7a35tcpg236-1
// --------------------------------------------------------------------------------
`timescale 1 ps / 1 ps

(* CHECK_LICENSE_TYPE = "design_1_or_0_1,or_gate,{}" *) (* downgradeipidentifiedwarnings = "yes" *) (* ip_definition_source = "package_project" *) 
(* x_core_info = "or_gate,Vivado 2018.2.1" *) 
(* NotValidForBitStream *)
module design_1_or_0_0
   (I0,
    I1,
    I2,
    O);
  (* x_interface_info = "xilinx.com:signal:data:1.0 I0 DATA" *) (* x_interface_parameter = "XIL_INTERFACENAME I0, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef" *) input I0;
  (* x_interface_info = "xilinx.com:signal:data:1.0 I1 DATA" *) (* x_interface_parameter = "XIL_INTERFACENAME I1, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef" *) input I1;
  (* x_interface_info = "xilinx.com:signal:data:1.0 I2 DATA" *) (* x_interface_parameter = "XIL_INTERFACENAME I2, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef" *) input I2;
  (* x_interface_info = "xilinx.com:signal:data:1.0 O DATA" *) (* x_interface_parameter = "XIL_INTERFACENAME O, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef" *) output O;

  wire I0;
  wire I1;
  wire I2;
  wire O;

  design_1_or_0_0_or_gate U0
       (.I0(I0),
        .I1(I1),
        .I2(I2),
        .O(O));
endmodule

module design_1_or_0_0_or_gate
   (O,
    I0,
    I1,
    I2);
  output O;
  input I0;
  input I1;
  input I2;

  wire I0;
  wire I1;
  wire I2;
  wire O;

  LUT3 #(
    .INIT(8'hFE)) 
    O_int
       (.I0(I0),
        .I1(I1),
        .I2(I2),
        .O(O));
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
