--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
--Date        : Thu Mar 26 17:08:38 2020
--Host        : THATSMEC804 running 64-bit major release  (build 9200)
--Command     : generate_target adder.bd
--Design      : adder
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity adder is
  port (
    DIR : in STD_LOGIC;
    INC : in STD_LOGIC;
    clk : in STD_LOGIC;
    reset : in STD_LOGIC;
    y0 : out STD_LOGIC;
    y1 : out STD_LOGIC;
    y2 : out STD_LOGIC
  );
  attribute CORE_GENERATION_INFO : string;
  attribute CORE_GENERATION_INFO of adder : entity is "adder,IP_Integrator,{x_ipVendor=xilinx.com,x_ipLibrary=BlockDiagram,x_ipName=adder,x_ipVersion=1.00.a,x_ipLanguage=VHDL,numBlks=5,numReposBlks=5,numNonXlnxBlks=3,numHierBlks=0,maxHierDepth=0,numSysgenBlks=0,numHlsBlks=0,numHdlrefBlks=2,numPkgbdBlks=0,bdsource=USER,synth_mode=OOC_per_IP}";
  attribute HW_HANDOFF : string;
  attribute HW_HANDOFF of adder : entity is "adder.hwdef";
end adder;

architecture STRUCTURE of adder is
  component adder_next_state_logic_0_0 is
  port (
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    INC : in STD_LOGIC;
    DIR : in STD_LOGIC;
    d0 : out STD_LOGIC;
    d1 : out STD_LOGIC;
    d2 : out STD_LOGIC
  );
  end component adder_next_state_logic_0_0;
  component adder_dff_0_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component adder_dff_0_0;
  component adder_dff_1_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component adder_dff_1_0;
  component adder_dff_2_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component adder_dff_2_0;
  component adder_output_logic_0_0 is
  port (
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    INC : in STD_LOGIC;
    DIR : in STD_LOGIC;
    y0 : out STD_LOGIC;
    y1 : out STD_LOGIC;
    y2 : out STD_LOGIC
  );
  end component adder_output_logic_0_0;
  signal DIR_0_1 : STD_LOGIC;
  signal INC_0_1 : STD_LOGIC;
  signal clk_0_1 : STD_LOGIC;
  signal dff_0_q : STD_LOGIC;
  signal dff_1_q : STD_LOGIC;
  signal dff_2_q : STD_LOGIC;
  signal next_state_logic_0_d0 : STD_LOGIC;
  signal next_state_logic_0_d1 : STD_LOGIC;
  signal next_state_logic_0_d2 : STD_LOGIC;
  signal output_logic_0_y0 : STD_LOGIC;
  signal output_logic_0_y1 : STD_LOGIC;
  signal output_logic_0_y2 : STD_LOGIC;
  signal reset_0_1 : STD_LOGIC;
begin
  DIR_0_1 <= DIR;
  INC_0_1 <= INC;
  clk_0_1 <= clk;
  reset_0_1 <= reset;
  y0 <= output_logic_0_y0;
  y1 <= output_logic_0_y1;
  y2 <= output_logic_0_y2;
dff_0: component adder_dff_0_0
     port map (
      ce => INC_0_1,
      clk => clk_0_1,
      d => next_state_logic_0_d0,
      q => dff_0_q,
      reset => reset_0_1
    );
dff_1: component adder_dff_1_0
     port map (
      ce => INC_0_1,
      clk => clk_0_1,
      d => next_state_logic_0_d1,
      q => dff_1_q,
      reset => reset_0_1
    );
dff_2: component adder_dff_2_0
     port map (
      ce => INC_0_1,
      clk => clk_0_1,
      d => next_state_logic_0_d2,
      q => dff_2_q,
      reset => reset_0_1
    );
next_state_logic_0: component adder_next_state_logic_0_0
     port map (
      DIR => DIR_0_1,
      INC => INC_0_1,
      d0 => next_state_logic_0_d0,
      d1 => next_state_logic_0_d1,
      d2 => next_state_logic_0_d2,
      q0 => dff_0_q,
      q1 => dff_1_q,
      q2 => dff_2_q
    );
output_logic_0: component adder_output_logic_0_0
     port map (
      DIR => DIR_0_1,
      INC => INC_0_1,
      q0 => dff_0_q,
      q1 => dff_1_q,
      q2 => dff_2_q,
      y0 => output_logic_0_y0,
      y1 => output_logic_0_y1,
      y2 => output_logic_0_y2
    );
end STRUCTURE;
