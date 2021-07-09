--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
--Date        : Tue Feb 25 08:24:06 2020
--Host        : PC-1042-103 running 64-bit major release  (build 9200)
--Command     : generate_target combi_wrapper.bd
--Design      : combi_wrapper
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity combi_wrapper is
  port (
    a : in STD_LOGIC;
    b : in STD_LOGIC;
    c : in STD_LOGIC;
    d : in STD_LOGIC;
    s : out STD_LOGIC
  );
end combi_wrapper;

architecture STRUCTURE of combi_wrapper is
  component combi is
  port (
    b : in STD_LOGIC;
    d : in STD_LOGIC;
    c : in STD_LOGIC;
    a : in STD_LOGIC;
    s : out STD_LOGIC
  );
  end component combi;
begin
combi_i: component combi
     port map (
      a => a,
      b => b,
      c => c,
      d => d,
      s => s
    );
end STRUCTURE;
