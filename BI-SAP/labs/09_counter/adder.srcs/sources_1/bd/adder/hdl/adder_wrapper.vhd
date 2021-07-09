--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
--Date        : Thu Mar 26 17:08:38 2020
--Host        : THATSMEC804 running 64-bit major release  (build 9200)
--Command     : generate_target adder_wrapper.bd
--Design      : adder_wrapper
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity adder_wrapper is
  port (
    DIR : in STD_LOGIC;
    INC : in STD_LOGIC;
    clk : in STD_LOGIC;
    reset : in STD_LOGIC;
    y0 : out STD_LOGIC;
    y1 : out STD_LOGIC;
    y2 : out STD_LOGIC
  );
end adder_wrapper;

architecture STRUCTURE of adder_wrapper is
  component adder is
  port (
    DIR : in STD_LOGIC;
    INC : in STD_LOGIC;
    y0 : out STD_LOGIC;
    y1 : out STD_LOGIC;
    y2 : out STD_LOGIC;
    clk : in STD_LOGIC;
    reset : in STD_LOGIC
  );
  end component adder;
begin
adder_i: component adder
     port map (
      DIR => DIR,
      INC => INC,
      clk => clk,
      reset => reset,
      y0 => y0,
      y1 => y1,
      y2 => y2
    );
end STRUCTURE;
