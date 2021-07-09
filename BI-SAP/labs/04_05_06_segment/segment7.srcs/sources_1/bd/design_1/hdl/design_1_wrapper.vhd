--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
--Date        : Tue Mar  3 08:56:18 2020
--Host        : PC-1042-103 running 64-bit major release  (build 9200)
--Command     : generate_target design_1_wrapper.bd
--Design      : design_1_wrapper
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity design_1_wrapper is
  port (
    O_1 : out STD_LOGIC;
    a : in STD_LOGIC;
    an_0 : out STD_LOGIC;
    an_1 : out STD_LOGIC;
    an_2 : out STD_LOGIC;
    b : in STD_LOGIC;
    c : in STD_LOGIC;
    c_f : out STD_LOGIC;
    d : in STD_LOGIC;
    f_a : out STD_LOGIC;
    f_b : out STD_LOGIC;
    f_c : out STD_LOGIC;
    f_d : out STD_LOGIC;
    f_e : out STD_LOGIC;
    f_g : out STD_LOGIC
  );
end design_1_wrapper;

architecture STRUCTURE of design_1_wrapper is
  component design_1 is
  port (
    a : in STD_LOGIC;
    b : in STD_LOGIC;
    c : in STD_LOGIC;
    d : in STD_LOGIC;
    f_a : out STD_LOGIC;
    f_b : out STD_LOGIC;
    f_c : out STD_LOGIC;
    f_d : out STD_LOGIC;
    f_e : out STD_LOGIC;
    f_g : out STD_LOGIC;
    an_0 : out STD_LOGIC;
    an_1 : out STD_LOGIC;
    an_2 : out STD_LOGIC;
    O_1 : out STD_LOGIC;
    c_f : out STD_LOGIC
  );
  end component design_1;
begin
design_1_i: component design_1
     port map (
      O_1 => O_1,
      a => a,
      an_0 => an_0,
      an_1 => an_1,
      an_2 => an_2,
      b => b,
      c => c,
      c_f => c_f,
      d => d,
      f_a => f_a,
      f_b => f_b,
      f_c => f_c,
      f_d => f_d,
      f_e => f_e,
      f_g => f_g
    );
end STRUCTURE;
