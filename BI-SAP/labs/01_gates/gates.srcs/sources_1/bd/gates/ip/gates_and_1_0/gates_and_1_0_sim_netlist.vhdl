-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
-- Date        : Wed Feb 19 18:18:43 2020
-- Host        : PC-1042-103 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim
--               c:/Users/wrzecond/Documents/zatimsap/gates/gates.srcs/sources_1/bd/gates/ip/gates_and_1_0/gates_and_1_0_sim_netlist.vhdl
-- Design      : gates_and_1_0
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a35tcpg236-1
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity gates_and_1_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of gates_and_1_0 : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of gates_and_1_0 : entity is "gates_and_1_0,and_gate,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of gates_and_1_0 : entity is "yes";
  attribute ip_definition_source : string;
  attribute ip_definition_source of gates_and_1_0 : entity is "package_project";
  attribute x_core_info : string;
  attribute x_core_info of gates_and_1_0 : entity is "and_gate,Vivado 2018.2.1";
end gates_and_1_0;

architecture STRUCTURE of gates_and_1_0 is
  attribute x_interface_info : string;
  attribute x_interface_info of I0 : signal is "xilinx.com:signal:data:1.0 I0 DATA";
  attribute x_interface_parameter : string;
  attribute x_interface_parameter of I0 : signal is "XIL_INTERFACENAME I0, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  attribute x_interface_info of I1 : signal is "xilinx.com:signal:data:1.0 I1 DATA";
  attribute x_interface_parameter of I1 : signal is "XIL_INTERFACENAME I1, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  attribute x_interface_info of O : signal is "xilinx.com:signal:data:1.0 O DATA";
  attribute x_interface_parameter of O : signal is "XIL_INTERFACENAME O, POLARITY ACTIVE_LOW, LAYERED_METADATA undef";
begin
O_INST_0: unisim.vcomponents.LUT2
    generic map(
      INIT => X"7"
    )
        port map (
      I0 => I0,
      I1 => I1,
      O => O
    );
end STRUCTURE;
