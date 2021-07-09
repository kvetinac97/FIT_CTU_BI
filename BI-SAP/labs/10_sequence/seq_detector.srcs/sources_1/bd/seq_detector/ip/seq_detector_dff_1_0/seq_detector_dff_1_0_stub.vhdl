-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
-- Date        : Thu Mar 26 19:00:48 2020
-- Host        : THATSMEC804 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode synth_stub -rename_top seq_detector_dff_1_0 -prefix
--               seq_detector_dff_1_0_ seq_detector_dff_0_0_stub.vhdl
-- Design      : seq_detector_dff_0_0
-- Purpose     : Stub declaration of top-level module interface
-- Device      : xc7a12ticsg325-1L
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity seq_detector_dff_1_0 is
  Port ( 
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );

end seq_detector_dff_1_0;

architecture stub of seq_detector_dff_1_0 is
attribute syn_black_box : boolean;
attribute black_box_pad_pin : string;
attribute syn_black_box of stub : architecture is true;
attribute black_box_pad_pin of stub : architecture is "d,clk,ce,reset,q";
attribute x_core_info : string;
attribute x_core_info of stub : architecture is "dff,Vivado 2018.2";
begin
end;
