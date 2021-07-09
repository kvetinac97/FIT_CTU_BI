-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
-- Date        : Tue Mar  3 08:23:07 2020
-- Host        : PC-1042-103 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode synth_stub
--               c:/testsap/segment7/segment7.srcs/sources_1/bd/design_1/ip/design_1_vcc_0_0/design_1_vcc_0_0_stub.vhdl
-- Design      : design_1_vcc_0_0
-- Purpose     : Stub declaration of top-level module interface
-- Device      : xc7a35tcpg236-1
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity design_1_vcc_0_0 is
  Port ( 
    O : out STD_LOGIC
  );

end design_1_vcc_0_0;

architecture stub of design_1_vcc_0_0 is
attribute syn_black_box : boolean;
attribute black_box_pad_pin : string;
attribute syn_black_box of stub : architecture is true;
attribute black_box_pad_pin of stub : architecture is "O";
attribute x_core_info : string;
attribute x_core_info of stub : architecture is "vcc,Vivado 2018.2.1";
begin
end;
