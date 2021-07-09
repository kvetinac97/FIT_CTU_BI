-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
-- Date        : Thu Mar 26 18:58:30 2020
-- Host        : THATSMEC804 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode synth_stub
--               c:/Users/kvetinac97/seq_detector/seq_detector.srcs/sources_1/bd/seq_detector/ip/seq_detector_next_state_logic_0_0/seq_detector_next_state_logic_0_0_stub.vhdl
-- Design      : seq_detector_next_state_logic_0_0
-- Purpose     : Stub declaration of top-level module interface
-- Device      : xc7a12ticsg325-1L
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity seq_detector_next_state_logic_0_0 is
  Port ( 
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    sw : in STD_LOGIC;
    d0 : out STD_LOGIC;
    d1 : out STD_LOGIC;
    d2 : out STD_LOGIC
  );

end seq_detector_next_state_logic_0_0;

architecture stub of seq_detector_next_state_logic_0_0 is
attribute syn_black_box : boolean;
attribute black_box_pad_pin : string;
attribute syn_black_box of stub : architecture is true;
attribute black_box_pad_pin of stub : architecture is "q0,q1,q2,sw,d0,d1,d2";
attribute x_core_info : string;
attribute x_core_info of stub : architecture is "next_state_logic,Vivado 2018.2";
begin
end;
