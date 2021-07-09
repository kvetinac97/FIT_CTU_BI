-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
-- Date        : Tue Mar  3 08:20:44 2020
-- Host        : PC-1042-103 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode synth_stub -rename_top design_1_or_0_0 -prefix
--               design_1_or_0_0_ design_1_or_0_1_stub.vhdl
-- Design      : design_1_or_0_1
-- Purpose     : Stub declaration of top-level module interface
-- Device      : xc7a35tcpg236-1
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity design_1_or_0_0 is
  Port ( 
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );

end design_1_or_0_0;

architecture stub of design_1_or_0_0 is
attribute syn_black_box : boolean;
attribute black_box_pad_pin : string;
attribute syn_black_box of stub : architecture is true;
attribute black_box_pad_pin of stub : architecture is "I0,I1,I2,O";
attribute x_core_info : string;
attribute x_core_info of stub : architecture is "or_gate,Vivado 2018.2.1";
begin
end;