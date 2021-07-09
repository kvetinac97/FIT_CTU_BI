-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
-- Date        : Thu Mar 26 18:58:30 2020
-- Host        : THATSMEC804 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim
--               c:/Users/kvetinac97/seq_detector/seq_detector.srcs/sources_1/bd/seq_detector/ip/seq_detector_next_state_logic_0_0/seq_detector_next_state_logic_0_0_sim_netlist.vhdl
-- Design      : seq_detector_next_state_logic_0_0
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a12ticsg325-1L
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector_next_state_logic_0_0_next_state_logic is
  port (
    d1 : out STD_LOGIC;
    d2 : out STD_LOGIC;
    q2 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    sw : in STD_LOGIC;
    q0 : in STD_LOGIC
  );
  attribute ORIG_REF_NAME : string;
  attribute ORIG_REF_NAME of seq_detector_next_state_logic_0_0_next_state_logic : entity is "next_state_logic";
end seq_detector_next_state_logic_0_0_next_state_logic;

architecture STRUCTURE of seq_detector_next_state_logic_0_0_next_state_logic is
  attribute SOFT_HLUTNM : string;
  attribute SOFT_HLUTNM of \d1__0\ : label is "soft_lutpair0";
  attribute SOFT_HLUTNM of \d2__0\ : label is "soft_lutpair0";
begin
\d1__0\: unisim.vcomponents.LUT4
    generic map(
      INIT => X"F440"
    )
        port map (
      I0 => q2,
      I1 => q1,
      I2 => sw,
      I3 => q0,
      O => d1
    );
\d2__0\: unisim.vcomponents.LUT4
    generic map(
      INIT => X"38B8"
    )
        port map (
      I0 => q1,
      I1 => sw,
      I2 => q0,
      I3 => q2,
      O => d2
    );
end STRUCTURE;
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector_next_state_logic_0_0 is
  port (
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    sw : in STD_LOGIC;
    d0 : out STD_LOGIC;
    d1 : out STD_LOGIC;
    d2 : out STD_LOGIC
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of seq_detector_next_state_logic_0_0 : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of seq_detector_next_state_logic_0_0 : entity is "seq_detector_next_state_logic_0_0,next_state_logic,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of seq_detector_next_state_logic_0_0 : entity is "yes";
  attribute ip_definition_source : string;
  attribute ip_definition_source of seq_detector_next_state_logic_0_0 : entity is "module_ref";
  attribute x_core_info : string;
  attribute x_core_info of seq_detector_next_state_logic_0_0 : entity is "next_state_logic,Vivado 2018.2";
end seq_detector_next_state_logic_0_0;

architecture STRUCTURE of seq_detector_next_state_logic_0_0 is
begin
U0: entity work.seq_detector_next_state_logic_0_0_next_state_logic
     port map (
      d1 => d1,
      d2 => d2,
      q0 => q0,
      q1 => q1,
      q2 => q2,
      sw => sw
    );
d0_INST_0: unisim.vcomponents.LUT4
    generic map(
      INIT => X"20FF"
    )
        port map (
      I0 => q0,
      I1 => q1,
      I2 => q2,
      I3 => sw,
      O => d0
    );
end STRUCTURE;
