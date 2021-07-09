--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
--Date        : Wed Feb 19 18:17:55 2020
--Host        : PC-1042-103 running 64-bit major release  (build 9200)
--Command     : generate_target gates_wrapper.bd
--Design      : gates_wrapper
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity gates_wrapper is
  port (
    A : in STD_LOGIC;
    B : in STD_LOGIC;
    C : in STD_LOGIC;
    D : in STD_LOGIC;
    E : in STD_LOGIC;
    F : in STD_LOGIC;
    G : in STD_LOGIC;
    H : in STD_LOGIC;
    P : out STD_LOGIC;
    Q : out STD_LOGIC;
    R : out STD_LOGIC;
    S : out STD_LOGIC
  );
end gates_wrapper;

architecture STRUCTURE of gates_wrapper is
  component gates is
  port (
    A : in STD_LOGIC;
    B : in STD_LOGIC;
    P : out STD_LOGIC;
    E : in STD_LOGIC;
    F : in STD_LOGIC;
    R : out STD_LOGIC;
    C : in STD_LOGIC;
    D : in STD_LOGIC;
    Q : out STD_LOGIC;
    G : in STD_LOGIC;
    H : in STD_LOGIC;
    S : out STD_LOGIC
  );
  end component gates;
begin
gates_i: component gates
     port map (
      A => A,
      B => B,
      C => C,
      D => D,
      E => E,
      F => F,
      G => G,
      H => H,
      P => P,
      Q => Q,
      R => R,
      S => S
    );
end STRUCTURE;
