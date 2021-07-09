--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
--Date        : Mon Mar 30 15:48:07 2020
--Host        : THATSMEC804 running 64-bit major release  (build 9200)
--Command     : generate_target trezor_wrapper.bd
--Design      : trezor_wrapper
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity trezor_wrapper is
  port (
    clk : in STD_LOGIC;
    down : in STD_LOGIC;
    left : in STD_LOGIC;
    reset : in STD_LOGIC;
    right : in STD_LOGIC;
    tb0 : out STD_LOGIC;
    tb1 : out STD_LOGIC;
    ts0 : out STD_LOGIC;
    ts1 : out STD_LOGIC;
    up : in STD_LOGIC;
    y : out STD_LOGIC
  );
end trezor_wrapper;

architecture STRUCTURE of trezor_wrapper is
  component trezor is
  port (
    y : out STD_LOGIC;
    clk : in STD_LOGIC;
    reset : in STD_LOGIC;
    up : in STD_LOGIC;
    right : in STD_LOGIC;
    down : in STD_LOGIC;
    left : in STD_LOGIC;
    tb1 : out STD_LOGIC;
    tb0 : out STD_LOGIC;
    ts1 : out STD_LOGIC;
    ts0 : out STD_LOGIC
  );
  end component trezor;
begin
trezor_i: component trezor
     port map (
      clk => clk,
      down => down,
      left => left,
      reset => reset,
      right => right,
      tb0 => tb0,
      tb1 => tb1,
      ts0 => ts0,
      ts1 => ts1,
      up => up,
      y => y
    );
end STRUCTURE;
