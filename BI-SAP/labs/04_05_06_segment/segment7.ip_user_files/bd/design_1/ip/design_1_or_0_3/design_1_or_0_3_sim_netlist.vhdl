-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
-- Date        : Tue Mar  3 08:20:44 2020
-- Host        : PC-1042-103 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim -rename_top design_1_or_0_3 -prefix
--               design_1_or_0_3_ design_1_or_0_1_sim_netlist.vhdl
-- Design      : design_1_or_0_1
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a35tcpg236-1
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity design_1_or_0_3_or_gate is
  port (
    O : out STD_LOGIC;
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC
  );
end design_1_or_0_3_or_gate;

architecture STRUCTURE of design_1_or_0_3_or_gate is
begin
O_int: unisim.vcomponents.LUT3
    generic map(
      INIT => X"FE"
    )
        port map (
      I0 => I0,
      I1 => I1,
      I2 => I2,
      O => O
    );
end STRUCTURE;
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity design_1_or_0_3 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of design_1_or_0_3 : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of design_1_or_0_3 : entity is "design_1_or_0_1,or_gate,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of design_1_or_0_3 : entity is "yes";
  attribute ip_definition_source : string;
  attribute ip_definition_source of design_1_or_0_3 : entity is "package_project";
  attribute x_core_info : string;
  attribute x_core_info of design_1_or_0_3 : entity is "or_gate,Vivado 2018.2.1";
end design_1_or_0_3;

architecture STRUCTURE of design_1_or_0_3 is
  attribute x_interface_info : string;
  attribute x_interface_info of I0 : signal is "xilinx.com:signal:data:1.0 I0 DATA";
  attribute x_interface_parameter : string;
  attribute x_interface_parameter of I0 : signal is "XIL_INTERFACENAME I0, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  attribute x_interface_info of I1 : signal is "xilinx.com:signal:data:1.0 I1 DATA";
  attribute x_interface_parameter of I1 : signal is "XIL_INTERFACENAME I1, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  attribute x_interface_info of I2 : signal is "xilinx.com:signal:data:1.0 I2 DATA";
  attribute x_interface_parameter of I2 : signal is "XIL_INTERFACENAME I2, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  attribute x_interface_info of O : signal is "xilinx.com:signal:data:1.0 O DATA";
  attribute x_interface_parameter of O : signal is "XIL_INTERFACENAME O, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
begin
U0: entity work.design_1_or_0_3_or_gate
     port map (
      I0 => I0,
      I1 => I1,
      I2 => I2,
      O => O
    );
end STRUCTURE;
