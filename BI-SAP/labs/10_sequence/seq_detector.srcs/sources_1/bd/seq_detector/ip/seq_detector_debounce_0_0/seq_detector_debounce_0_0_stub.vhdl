-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
-- Date        : Thu Mar 26 19:01:49 2020
-- Host        : THATSMEC804 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode synth_stub
--               c:/Users/kvetinac97/seq_detector/seq_detector.srcs/sources_1/bd/seq_detector/ip/seq_detector_debounce_0_0/seq_detector_debounce_0_0_stub.vhdl
-- Design      : seq_detector_debounce_0_0
-- Purpose     : Stub declaration of top-level module interface
-- Device      : xc7a12ticsg325-1L
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity seq_detector_debounce_0_0 is
  Port ( 
    clk : in STD_LOGIC;
    tl_in : in STD_LOGIC;
    tl_out : out STD_LOGIC
  );

end seq_detector_debounce_0_0;

architecture stub of seq_detector_debounce_0_0 is
attribute syn_black_box : boolean;
attribute black_box_pad_pin : string;
attribute syn_black_box of stub : architecture is true;
attribute black_box_pad_pin of stub : architecture is "clk,tl_in,tl_out";
attribute x_core_info : string;
attribute x_core_info of stub : architecture is "debounce,Vivado 2018.2";
begin
end;
