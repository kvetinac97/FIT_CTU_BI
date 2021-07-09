--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
--Date        : Thu Mar 26 19:26:13 2020
--Host        : THATSMEC804 running 64-bit major release  (build 9200)
--Command     : generate_target seq_detector_wrapper.bd
--Design      : seq_detector_wrapper
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector_wrapper is
  port (
    btn : in STD_LOGIC;
    clk : in STD_LOGIC;
    detect : out STD_LOGIC;
    reset : in STD_LOGIC;
    s0 : out STD_LOGIC;
    s1 : out STD_LOGIC;
    s2 : out STD_LOGIC;
    switch : in STD_LOGIC
  );
end seq_detector_wrapper;

architecture STRUCTURE of seq_detector_wrapper is
  component seq_detector is
  port (
    clk : in STD_LOGIC;
    btn : in STD_LOGIC;
    reset : in STD_LOGIC;
    switch : in STD_LOGIC;
    detect : out STD_LOGIC;
    s0 : out STD_LOGIC;
    s1 : out STD_LOGIC;
    s2 : out STD_LOGIC
  );
  end component seq_detector;
begin
seq_detector_i: component seq_detector
     port map (
      btn => btn,
      clk => clk,
      detect => detect,
      reset => reset,
      s0 => s0,
      s1 => s1,
      s2 => s2,
      switch => switch
    );
end STRUCTURE;
