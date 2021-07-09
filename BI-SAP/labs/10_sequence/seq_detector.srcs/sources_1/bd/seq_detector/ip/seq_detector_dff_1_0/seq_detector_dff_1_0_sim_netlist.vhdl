-- Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
-- --------------------------------------------------------------------------------
-- Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
-- Date        : Thu Mar 26 19:00:48 2020
-- Host        : THATSMEC804 running 64-bit major release  (build 9200)
-- Command     : write_vhdl -force -mode funcsim -rename_top seq_detector_dff_1_0 -prefix
--               seq_detector_dff_1_0_ seq_detector_dff_0_0_sim_netlist.vhdl
-- Design      : seq_detector_dff_0_0
-- Purpose     : This VHDL netlist is a functional simulation representation of the design and should not be modified or
--               synthesized. This netlist cannot be used for SDF annotated simulation.
-- Device      : xc7a12ticsg325-1L
-- --------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector_dff_1_0_dff is
  port (
    q : out STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    d : in STD_LOGIC;
    reset : in STD_LOGIC
  );
end seq_detector_dff_1_0_dff;

architecture STRUCTURE of seq_detector_dff_1_0_dff is
  signal \^q\ : STD_LOGIC;
  signal q_i_1_n_0 : STD_LOGIC;
begin
  q <= \^q\;
q_i_1: unisim.vcomponents.LUT4
    generic map(
      INIT => X"00E2"
    )
        port map (
      I0 => \^q\,
      I1 => ce,
      I2 => d,
      I3 => reset,
      O => q_i_1_n_0
    );
q_reg: unisim.vcomponents.FDRE
     port map (
      C => clk,
      CE => '1',
      D => q_i_1_n_0,
      Q => \^q\,
      R => '0'
    );
end STRUCTURE;
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector_dff_1_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  attribute NotValidForBitStream : boolean;
  attribute NotValidForBitStream of seq_detector_dff_1_0 : entity is true;
  attribute CHECK_LICENSE_TYPE : string;
  attribute CHECK_LICENSE_TYPE of seq_detector_dff_1_0 : entity is "seq_detector_dff_0_0,dff,{}";
  attribute downgradeipidentifiedwarnings : string;
  attribute downgradeipidentifiedwarnings of seq_detector_dff_1_0 : entity is "yes";
  attribute ip_definition_source : string;
  attribute ip_definition_source of seq_detector_dff_1_0 : entity is "package_project";
  attribute x_core_info : string;
  attribute x_core_info of seq_detector_dff_1_0 : entity is "dff,Vivado 2018.2";
end seq_detector_dff_1_0;

architecture STRUCTURE of seq_detector_dff_1_0 is
  attribute x_interface_info : string;
  attribute x_interface_info of d : signal is "xilinx.com:signal:data:1.0 D DATA";
  attribute x_interface_parameter : string;
  attribute x_interface_parameter of d : signal is "XIL_INTERFACENAME D, LAYERED_METADATA undef";
  attribute x_interface_info of q : signal is "xilinx.com:signal:data:1.0 Q DATA";
  attribute x_interface_parameter of q : signal is "XIL_INTERFACENAME Q, LAYERED_METADATA undef";
begin
U0: entity work.seq_detector_dff_1_0_dff
     port map (
      ce => ce,
      clk => clk,
      d => d,
      q => q,
      reset => reset
    );
end STRUCTURE;
