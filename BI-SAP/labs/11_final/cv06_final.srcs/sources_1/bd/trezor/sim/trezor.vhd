--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2 (win64) Build 2258646 Thu Jun 14 20:03:12 MDT 2018
--Date        : Mon Mar 30 15:48:07 2020
--Host        : THATSMEC804 running 64-bit major release  (build 9200)
--Command     : generate_target trezor.bd
--Design      : trezor
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity automat_0_imp_1WIPABH is
  port (
    I0_0 : in STD_LOGIC;
    clk_0 : in STD_LOGIC;
    down_0 : in STD_LOGIC;
    left_0 : in STD_LOGIC;
    reset_0 : in STD_LOGIC;
    right_0 : in STD_LOGIC;
    tb0 : out STD_LOGIC;
    tb1 : out STD_LOGIC;
    ts1 : out STD_LOGIC;
    up_0 : in STD_LOGIC;
    y : out STD_LOGIC
  );
end automat_0_imp_1WIPABH;

architecture STRUCTURE of automat_0_imp_1WIPABH is
  component trezor_and_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component trezor_and_0_0;
  component trezor_trezorKoder_0_0 is
  port (
    up : in STD_LOGIC;
    right : in STD_LOGIC;
    down : in STD_LOGIC;
    left : in STD_LOGIC;
    buttons1 : out STD_LOGIC;
    buttons0 : out STD_LOGIC
  );
  end component trezor_trezorKoder_0_0;
  component trezor_dff_0_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component trezor_dff_0_0;
  component trezor_next_state_logic_0_0 is
  port (
    buttons1 : in STD_LOGIC;
    buttons0 : in STD_LOGIC;
    q1 : in STD_LOGIC;
    q0 : in STD_LOGIC;
    d1 : out STD_LOGIC;
    d0 : out STD_LOGIC
  );
  end component trezor_next_state_logic_0_0;
  component trezor_dff_1_0 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    ce : in STD_LOGIC;
    reset : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component trezor_dff_1_0;
  component trezor_and_0_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component trezor_and_0_1;
  component trezor_dff_0_1 is
  port (
    d : in STD_LOGIC;
    clk : in STD_LOGIC;
    q : out STD_LOGIC
  );
  end component trezor_dff_0_1;
  signal I0_0_1 : STD_LOGIC;
  signal clk_0_1 : STD_LOGIC;
  signal dff_0_q : STD_LOGIC;
  signal dff_1_q : STD_LOGIC;
  signal dff_2_q : STD_LOGIC;
  signal down_0_1 : STD_LOGIC;
  signal edge_detector_O : STD_LOGIC;
  signal left_0_1 : STD_LOGIC;
  signal next_state_logic_0_d0 : STD_LOGIC;
  signal next_state_logic_0_d1 : STD_LOGIC;
  signal output_state_logic_O : STD_LOGIC;
  signal reset_0_1 : STD_LOGIC;
  signal right_0_1 : STD_LOGIC;
  signal trezorKoder_0_buttons0 : STD_LOGIC;
  signal trezorKoder_0_buttons1 : STD_LOGIC;
  signal up_0_1 : STD_LOGIC;
begin
  I0_0_1 <= I0_0;
  clk_0_1 <= clk_0;
  down_0_1 <= down_0;
  left_0_1 <= left_0;
  reset_0_1 <= reset_0;
  right_0_1 <= right_0;
  tb0 <= trezorKoder_0_buttons0;
  tb1 <= trezorKoder_0_buttons1;
  ts1 <= dff_0_q;
  up_0_1 <= up_0;
  y <= output_state_logic_O;
dff_0: component trezor_dff_0_0
     port map (
      ce => edge_detector_O,
      clk => clk_0_1,
      d => next_state_logic_0_d0,
      q => dff_0_q,
      reset => reset_0_1
    );
dff_1: component trezor_dff_1_0
     port map (
      ce => edge_detector_O,
      clk => clk_0_1,
      d => next_state_logic_0_d1,
      q => dff_1_q,
      reset => reset_0_1
    );
dff_2: component trezor_dff_0_1
     port map (
      clk => clk_0_1,
      d => I0_0_1,
      q => dff_2_q
    );
edge_detector: component trezor_and_0_1
     port map (
      I0 => I0_0_1,
      I1 => dff_2_q,
      O => edge_detector_O
    );
next_state_logic_0: component trezor_next_state_logic_0_0
     port map (
      buttons0 => trezorKoder_0_buttons0,
      buttons1 => trezorKoder_0_buttons1,
      d0 => next_state_logic_0_d0,
      d1 => next_state_logic_0_d1,
      q0 => dff_0_q,
      q1 => dff_1_q
    );
output_state_logic: component trezor_and_0_0
     port map (
      I0 => dff_1_q,
      I1 => dff_0_q,
      O => output_state_logic_O
    );
trezorKoder_0: component trezor_trezorKoder_0_0
     port map (
      buttons0 => trezorKoder_0_buttons0,
      buttons1 => trezorKoder_0_buttons1,
      down => down_0_1,
      left => left_0_1,
      right => right_0_1,
      up => up_0_1
    );
end STRUCTURE;
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity trezor is
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
  attribute CORE_GENERATION_INFO : string;
  attribute CORE_GENERATION_INFO of trezor : entity is "trezor,IP_Integrator,{x_ipVendor=xilinx.com,x_ipLibrary=BlockDiagram,x_ipName=trezor,x_ipVersion=1.00.a,x_ipLanguage=VHDL,numBlks=13,numReposBlks=12,numNonXlnxBlks=10,numHierBlks=1,maxHierDepth=1,numSysgenBlks=0,numHlsBlks=0,numHdlrefBlks=2,numPkgbdBlks=0,bdsource=USER,synth_mode=OOC_per_IP}";
  attribute HW_HANDOFF : string;
  attribute HW_HANDOFF of trezor : entity is "trezor.hwdef";
end trezor;

architecture STRUCTURE of trezor is
  component trezor_debounce_0_0 is
  port (
    clk : in STD_LOGIC;
    tl_in : in STD_LOGIC;
    tl_out : out STD_LOGIC
  );
  end component trezor_debounce_0_0;
  component trezor_debounce_1_0 is
  port (
    clk : in STD_LOGIC;
    tl_in : in STD_LOGIC;
    tl_out : out STD_LOGIC
  );
  end component trezor_debounce_1_0;
  component trezor_debounce_2_0 is
  port (
    clk : in STD_LOGIC;
    tl_in : in STD_LOGIC;
    tl_out : out STD_LOGIC
  );
  end component trezor_debounce_2_0;
  component trezor_debounce_3_0 is
  port (
    clk : in STD_LOGIC;
    tl_in : in STD_LOGIC;
    tl_out : out STD_LOGIC
  );
  end component trezor_debounce_3_0;
  component trezor_or_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component trezor_or_0_0;
  signal I0_0_1 : STD_LOGIC;
  signal automat_0_tb0 : STD_LOGIC;
  signal automat_0_tb1 : STD_LOGIC;
  signal automat_0_ts1 : STD_LOGIC;
  signal clk_0_1 : STD_LOGIC;
  signal down_0_1 : STD_LOGIC;
  signal left_0_1 : STD_LOGIC;
  signal output_state_logic_O : STD_LOGIC;
  signal reset_0_1 : STD_LOGIC;
  signal right_0_1 : STD_LOGIC;
  signal tl_in_0_1 : STD_LOGIC;
  signal tl_in_0_2 : STD_LOGIC;
  signal tl_in_0_3 : STD_LOGIC;
  signal tl_in_0_4 : STD_LOGIC;
  signal up_0_1 : STD_LOGIC;
  attribute X_INTERFACE_INFO : string;
  attribute X_INTERFACE_INFO of y : signal is "xilinx.com:signal:data:1.0 DATA.Y DATA";
  attribute X_INTERFACE_PARAMETER : string;
  attribute X_INTERFACE_PARAMETER of y : signal is "XIL_INTERFACENAME DATA.Y, LAYERED_METADATA undef";
begin
  clk_0_1 <= clk;
  reset_0_1 <= reset;
  tb0 <= automat_0_tb0;
  tb1 <= automat_0_tb1;
  tl_in_0_1 <= up;
  tl_in_0_2 <= right;
  tl_in_0_3 <= down;
  tl_in_0_4 <= left;
  ts0 <= automat_0_ts1;
  ts1 <= automat_0_ts1;
  y <= output_state_logic_O;
automat_0: entity work.automat_0_imp_1WIPABH
     port map (
      I0_0 => I0_0_1,
      clk_0 => clk_0_1,
      down_0 => down_0_1,
      left_0 => left_0_1,
      reset_0 => reset_0_1,
      right_0 => right_0_1,
      tb0 => automat_0_tb0,
      tb1 => automat_0_tb1,
      ts1 => automat_0_ts1,
      up_0 => up_0_1,
      y => output_state_logic_O
    );
debounce_0: component trezor_debounce_0_0
     port map (
      clk => clk_0_1,
      tl_in => tl_in_0_1,
      tl_out => up_0_1
    );
debounce_1: component trezor_debounce_1_0
     port map (
      clk => clk_0_1,
      tl_in => tl_in_0_2,
      tl_out => right_0_1
    );
debounce_2: component trezor_debounce_2_0
     port map (
      clk => clk_0_1,
      tl_in => tl_in_0_3,
      tl_out => down_0_1
    );
debounce_3: component trezor_debounce_3_0
     port map (
      clk => clk_0_1,
      tl_in => tl_in_0_4,
      tl_out => left_0_1
    );
or_0: component trezor_or_0_0
     port map (
      I0 => tl_in_0_1,
      I1 => tl_in_0_2,
      I2 => tl_in_0_3,
      I3 => tl_in_0_4,
      O => I0_0_1
    );
end STRUCTURE;
