--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
--Date        : Thu Mar 26 19:26:12 2020
--Host        : THATSMEC804 running 64-bit major release  (build 9200)
--Command     : generate_target seq_detector.bd
--Design      : seq_detector
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity seq_detector is
  port (
    btn : in STD_LOGIC;
    clk : in STD_LOGIC;
    detect : out STD_LOGIC;
    reset : in STD_LOGIC;
    s0 : out STD_LOGIC;
    s1 : out STD_LOGIC;
    s2 : out STD_LOGIC;
    switch : in STD_LOGIC
  );
  attribute CORE_GENERATION_INFO : string;
  attribute CORE_GENERATION_INFO of seq_detector : entity is "seq_detector,IP_Integrator,{x_ipVendor=xilinx.com,x_ipLibrary=BlockDiagram,x_ipName=seq_detector,x_ipVersion=1.00.a,x_ipLanguage=VHDL,numBlks=7,numReposBlks=7,numNonXlnxBlks=5,numHierBlks=0,maxHierDepth=0,numSysgenBlks=0,numHlsBlks=0,numHdlrefBlks=2,numPkgbdBlks=0,bdsource=USER,synth_mode=OOC_per_IP}";
  attribute HW_HANDOFF : string;
  attribute HW_HANDOFF of seq_detector : entity is "seq_detector.hwdef";
end seq_detector;

architecture STRUCTURE of seq_detector is
  component seq_detector_next_state_logic_0_0 is
  port (
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    sw : in STD_LOGIC;
    d0 : out STD_LOGIC;
    d1 : out STD_LOGIC;
    d2 : out STD_LOGIC
  );
  end component seq_detector_next_state_logic_0_0;
  component seq_detector_output_logic_0_0 is
  port (
    q0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q2 : in STD_LOGIC;
    sw : in STD_LOGIC;
    y : out STD_LOGIC
  );
  end component seq_detector_output_logic_0_0;
  component seq_detector_dff_0_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component seq_detector_dff_0_0;
  component seq_detector_dff_1_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component seq_detector_dff_1_0;
  component seq_detector_dff_2_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component seq_detector_dff_2_0;
  component seq_detector_debounce_0_0 is
  port (
    clk : in STD_LOGIC;
    tl_in : in STD_LOGIC;
    tl_out : out STD_LOGIC
  );
  end component seq_detector_debounce_0_0;
  component seq_detector_vcc_0_0 is
  port (
    O : out STD_LOGIC
  );
  end component seq_detector_vcc_0_0;
  signal clk_0_1 : STD_LOGIC;
  signal debounce_0_tl_out : STD_LOGIC;
  signal dff_0_q : STD_LOGIC;
  signal dff_1_q : STD_LOGIC;
  signal dff_2_q : STD_LOGIC;
  signal next_state_logic_0_d0 : STD_LOGIC;
  signal next_state_logic_0_d1 : STD_LOGIC;
  signal next_state_logic_0_d2 : STD_LOGIC;
  signal output_logic_0_y : STD_LOGIC;
  signal reset_0_1 : STD_LOGIC;
  signal sw_1_1 : STD_LOGIC;
  signal tl_in_0_1 : STD_LOGIC;
  signal vcc_0_O : STD_LOGIC;
  attribute X_INTERFACE_INFO : string;
  attribute X_INTERFACE_INFO of s0 : signal is "xilinx.com:signal:data:1.0 DATA.S0 DATA";
  attribute X_INTERFACE_PARAMETER : string;
  attribute X_INTERFACE_PARAMETER of s0 : signal is "XIL_INTERFACENAME DATA.S0, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of s1 : signal is "xilinx.com:signal:data:1.0 DATA.S1 DATA";
  attribute X_INTERFACE_PARAMETER of s1 : signal is "XIL_INTERFACENAME DATA.S1, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of s2 : signal is "xilinx.com:signal:data:1.0 DATA.S2 DATA";
  attribute X_INTERFACE_PARAMETER of s2 : signal is "XIL_INTERFACENAME DATA.S2, LAYERED_METADATA undef";
begin
  clk_0_1 <= clk;
  detect <= output_logic_0_y;
  reset_0_1 <= reset;
  s0 <= dff_0_q;
  s1 <= dff_1_q;
  s2 <= dff_2_q;
  sw_1_1 <= switch;
  tl_in_0_1 <= btn;
debounce_0: component seq_detector_debounce_0_0
     port map (
      clk => clk_0_1,
      tl_in => tl_in_0_1,
      tl_out => debounce_0_tl_out
    );
dff_0: component seq_detector_dff_0_0
     port map (
      ce => vcc_0_O,
      clk => debounce_0_tl_out,
      d => next_state_logic_0_d0,
      q => dff_0_q,
      reset => reset_0_1
    );
dff_1: component seq_detector_dff_1_0
     port map (
      ce => vcc_0_O,
      clk => debounce_0_tl_out,
      d => next_state_logic_0_d1,
      q => dff_1_q,
      reset => reset_0_1
    );
dff_2: component seq_detector_dff_2_0
     port map (
      ce => vcc_0_O,
      clk => debounce_0_tl_out,
      d => next_state_logic_0_d2,
      q => dff_2_q,
      reset => reset_0_1
    );
next_state_logic_0: component seq_detector_next_state_logic_0_0
     port map (
      d0 => next_state_logic_0_d0,
      d1 => next_state_logic_0_d1,
      d2 => next_state_logic_0_d2,
      q0 => dff_0_q,
      q1 => dff_1_q,
      q2 => dff_2_q,
      sw => sw_1_1
    );
output_logic_0: component seq_detector_output_logic_0_0
     port map (
      q0 => dff_0_q,
      q1 => dff_1_q,
      q2 => dff_2_q,
      sw => sw_1_1,
      y => output_logic_0_y
    );
vcc_0: component seq_detector_vcc_0_0
     port map (
      O => vcc_0_O
    );
end STRUCTURE;
