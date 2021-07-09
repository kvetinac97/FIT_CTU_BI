-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
-- Date        : Thu Mar 26 18:59:26 2020
-- Host        : THATSMEC804 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim
--               c:/Users/kvetinac97/seq_detector/seq_detector.srcs/sources_1/bd/seq_detector/ip/seq_detector_output_logic_0_0/seq_detector_output_logic_0_0_sim_netlist.vhdl
-- Design      : seq_detector_output_logic_0_0
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a12ticsg325-1L
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector_output_logic_0_0 is
  port (
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    sw : in STD_LOGIC;
    y : out STD_LOGIC
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of seq_detector_output_logic_0_0 : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of seq_detector_output_logic_0_0 : entity is "seq_detector_output_logic_0_0,output_logic,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of seq_detector_output_logic_0_0 : entity is "yes";
  attribute ip_definition_source : string;
  attribute ip_definition_source of seq_detector_output_logic_0_0 : entity is "module_ref";
  attribute x_core_info : string;
  attribute x_core_info of seq_detector_output_logic_0_0 : entity is "output_logic,Vivado 2018.2";
end seq_detector_output_logic_0_0;

architecture STRUCTURE of seq_detector_output_logic_0_0 is
begin
y_INST_0: unisim.vcomponents.LUT3
    generic map(
      INIT => X"90"
    )
        port map (
      I0 => q0,
      I1 => q1,
      I2 => q2,
      O => y
    );
end STRUCTURE;
