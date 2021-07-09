-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
-- Date        : Tue Mar  3 08:23:07 2020
-- Host        : PC-1042-103 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim -rename_top design_1_vcc_0_2 -prefix
--               design_1_vcc_0_2_ design_1_vcc_0_0_sim_netlist.vhdl
-- Design      : design_1_vcc_0_0
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a35tcpg236-1
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity design_1_vcc_0_2 is
  port (
    O : out STD_LOGIC
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of design_1_vcc_0_2 : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of design_1_vcc_0_2 : entity is "design_1_vcc_0_0,vcc,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of design_1_vcc_0_2 : entity is "yes";
  attribute ip_definition_source : string;
  attribute ip_definition_source of design_1_vcc_0_2 : entity is "package_project";
  attribute x_core_info : string;
  attribute x_core_info of design_1_vcc_0_2 : entity is "vcc,Vivado 2018.2.1";
end design_1_vcc_0_2;

architecture STRUCTURE of design_1_vcc_0_2 is
  signal \<const1>\ : STD_LOGIC;
begin
  O <= \<const1>\;
VCC: unisim.vcomponents.VCC
     port map (
      P => \<const1>\
    );
end STRUCTURE;
