--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
--Date        : Tue Mar  3 08:56:18 2020
--Host        : PC-1042-103 running 64-bit major release  (build 9200)
--Command     : generate_target design_1.bd
--Design      : design_1
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity design_1 is
  port (
    O_1 : out STD_LOGIC;
    a : in STD_LOGIC;
    an_0 : out STD_LOGIC;
    an_1 : out STD_LOGIC;
    an_2 : out STD_LOGIC;
    b : in STD_LOGIC;
    c : in STD_LOGIC;
    c_f : out STD_LOGIC;
    d : in STD_LOGIC;
    f_a : out STD_LOGIC;
    f_b : out STD_LOGIC;
    f_c : out STD_LOGIC;
    f_d : out STD_LOGIC;
    f_e : out STD_LOGIC;
    f_g : out STD_LOGIC
  );
  attribute CORE_GENERATION_INFO : string;
  attribute CORE_GENERATION_INFO of design_1 : entity is "design_1,IP_Integrator,{x_ipVendor=xilinx.com,x_ipLibrary=BlockDiagram,x_ipName=design_1,x_ipVersion=1.00.a,x_ipLanguage=VHDL,numBlks=37,numReposBlks=37,numNonXlnxBlks=37,numHierBlks=0,maxHierDepth=0,numSysgenBlks=0,numHlsBlks=0,numHdlrefBlks=0,numPkgbdBlks=0,bdsource=USER,synth_mode=OOC_per_IP}";
  attribute HW_HANDOFF : string;
  attribute HW_HANDOFF of design_1 : entity is "design_1.hwdef";
end design_1;

architecture STRUCTURE of design_1 is
  component design_1_and_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_0;
  component design_1_and_1_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_0;
  component design_1_and_2_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_2_0;
  component design_1_xor_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_xor_0_0;
  component design_1_and_0_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_1;
  component design_1_and_1_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_1;
  component design_1_xor_0_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_xor_0_1;
  component design_1_and_0_2 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_2;
  component design_1_and_0_3 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_3;
  component design_1_or_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_or_0_0;
  component design_1_and_0_4 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_4;
  component design_1_and_1_2 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_2;
  component design_1_and_2_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_2_1;
  component design_1_or_0_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_or_0_1;
  component design_1_and_0_5 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_5;
  component design_1_and_1_3 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_3;
  component design_1_and_2_2 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_2_2;
  component design_1_and_3_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_3_0;
  component design_1_xor_0_2 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_xor_0_2;
  component design_1_or_0_2 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_or_0_2;
  component design_1_and_0_6 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_6;
  component design_1_and_1_4 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_4;
  component design_1_and_2_3 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_2_3;
  component design_1_or_0_3 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_or_0_3;
  component design_1_and_0_7 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_7;
  component design_1_and_1_5 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_5;
  component design_1_and_2_4 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_2_4;
  component design_1_or_0_4 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_or_0_4;
  component design_1_gnd_0_0 is
  port (
    O : out STD_LOGIC
  );
  end component design_1_gnd_0_0;
  component design_1_vcc_0_0 is
  port (
    O : out STD_LOGIC
  );
  end component design_1_vcc_0_0;
  component design_1_vcc_0_1 is
  port (
    O : out STD_LOGIC
  );
  end component design_1_vcc_0_1;
  component design_1_vcc_0_2 is
  port (
    O : out STD_LOGIC
  );
  end component design_1_vcc_0_2;
  component design_1_and_0_8 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_0_8;
  component design_1_and_1_6 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_1_6;
  component design_1_and_2_5 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_2_5;
  component design_1_and_3_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_and_3_1;
  component design_1_or_0_5 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    I3 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component design_1_or_0_5;
  signal I0_0_1 : STD_LOGIC;
  signal I1_0_1 : STD_LOGIC;
  signal I1_0_2 : STD_LOGIC;
  signal I2_0_1 : STD_LOGIC;
  signal and0_O : STD_LOGIC;
  signal and10_O : STD_LOGIC;
  signal and11_O : STD_LOGIC;
  signal and12_O : STD_LOGIC;
  signal and13_O : STD_LOGIC;
  signal and14_O : STD_LOGIC;
  signal and15_O : STD_LOGIC;
  signal and16_O : STD_LOGIC;
  signal and17_O : STD_LOGIC;
  signal and18_O : STD_LOGIC;
  signal and19_O : STD_LOGIC;
  signal and1_O : STD_LOGIC;
  signal and20_O : STD_LOGIC;
  signal and21_O : STD_LOGIC;
  signal and22_O : STD_LOGIC;
  signal and23_O : STD_LOGIC;
  signal and2_O : STD_LOGIC;
  signal and3_O : STD_LOGIC;
  signal and4_O : STD_LOGIC;
  signal and5_O : STD_LOGIC;
  signal and6_O : STD_LOGIC;
  signal and7_O : STD_LOGIC;
  signal and8_O : STD_LOGIC;
  signal and9_O : STD_LOGIC;
  signal gnd_0_O : STD_LOGIC;
  signal or0_O : STD_LOGIC;
  signal or1_O : STD_LOGIC;
  signal or2_O : STD_LOGIC;
  signal or3_O : STD_LOGIC;
  signal or4_O : STD_LOGIC;
  signal or5_O : STD_LOGIC;
  signal vcc_0_O : STD_LOGIC;
  signal vcc_1_O : STD_LOGIC;
  signal vcc_2_O : STD_LOGIC;
  signal xor0_O : STD_LOGIC;
  signal xor1_O : STD_LOGIC;
  signal xor2_O : STD_LOGIC;
  attribute X_INTERFACE_INFO : string;
  attribute X_INTERFACE_INFO of a : signal is "xilinx.com:signal:data:1.0 DATA.A DATA";
  attribute X_INTERFACE_PARAMETER : string;
  attribute X_INTERFACE_PARAMETER of a : signal is "XIL_INTERFACENAME DATA.A, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of b : signal is "xilinx.com:signal:data:1.0 DATA.B DATA";
  attribute X_INTERFACE_PARAMETER of b : signal is "XIL_INTERFACENAME DATA.B, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of c : signal is "xilinx.com:signal:data:1.0 DATA.C DATA";
  attribute X_INTERFACE_PARAMETER of c : signal is "XIL_INTERFACENAME DATA.C, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of c_f : signal is "xilinx.com:signal:data:1.0 DATA.C_F DATA";
  attribute X_INTERFACE_PARAMETER of c_f : signal is "XIL_INTERFACENAME DATA.C_F, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of d : signal is "xilinx.com:signal:data:1.0 DATA.D DATA";
  attribute X_INTERFACE_PARAMETER of d : signal is "XIL_INTERFACENAME DATA.D, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of f_a : signal is "xilinx.com:signal:data:1.0 DATA.F_A DATA";
  attribute X_INTERFACE_PARAMETER of f_a : signal is "XIL_INTERFACENAME DATA.F_A, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of f_b : signal is "xilinx.com:signal:data:1.0 DATA.F_B DATA";
  attribute X_INTERFACE_PARAMETER of f_b : signal is "XIL_INTERFACENAME DATA.F_B, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of f_c : signal is "xilinx.com:signal:data:1.0 DATA.F_C DATA";
  attribute X_INTERFACE_PARAMETER of f_c : signal is "XIL_INTERFACENAME DATA.F_C, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of f_d : signal is "xilinx.com:signal:data:1.0 DATA.F_D DATA";
  attribute X_INTERFACE_PARAMETER of f_d : signal is "XIL_INTERFACENAME DATA.F_D, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of f_e : signal is "xilinx.com:signal:data:1.0 DATA.F_E DATA";
  attribute X_INTERFACE_PARAMETER of f_e : signal is "XIL_INTERFACENAME DATA.F_E, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of f_g : signal is "xilinx.com:signal:data:1.0 DATA.F_G DATA";
  attribute X_INTERFACE_PARAMETER of f_g : signal is "XIL_INTERFACENAME DATA.F_G, LAYERED_METADATA undef";
begin
  I0_0_1 <= a;
  I1_0_1 <= b;
  I1_0_2 <= c;
  I2_0_1 <= d;
  O_1 <= vcc_2_O;
  an_0 <= gnd_0_O;
  an_1 <= vcc_0_O;
  an_2 <= vcc_1_O;
  c_f <= or5_O;
  f_a <= xor0_O;
  f_b <= or0_O;
  f_c <= or1_O;
  f_d <= or2_O;
  f_e <= or3_O;
  f_g <= or4_O;
an_3: component design_1_vcc_0_2
     port map (
      O => vcc_2_O
    );
and0: component design_1_and_0_0
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      O => and0_O
    );
and1: component design_1_and_1_0
     port map (
      I0 => I1_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and1_O
    );
and10: component design_1_and_0_5
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I2_0_1,
      O => and10_O
    );
and11: component design_1_and_1_3
     port map (
      I0 => I1_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and11_O
    );
and12: component design_1_and_2_2
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      O => and12_O
    );
and13: component design_1_and_3_0
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      I3 => I2_0_1,
      O => and13_O
    );
and14: component design_1_and_0_6
     port map (
      I0 => I0_0_1,
      I1 => I2_0_1,
      O => and14_O
    );
and15: component design_1_and_1_4
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      O => and15_O
    );
and16: component design_1_and_2_3
     port map (
      I0 => I1_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and16_O
    );
and17: component design_1_and_0_7
     port map (
      I0 => I1_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and17_O
    );
and18: component design_1_and_1_5
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      I3 => I2_0_1,
      O => and18_O
    );
and19: component design_1_and_2_4
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      I3 => I2_0_1,
      O => and19_O
    );
and2: component design_1_and_2_0
     port map (
      I0 => I0_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and2_O
    );
and20: component design_1_and_0_8
     port map (
      I0 => I1_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and20_O
    );
and21: component design_1_and_1_6
     port map (
      I0 => I0_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and21_O
    );
and22: component design_1_and_2_5
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I2_0_1,
      O => and22_O
    );
and23: component design_1_and_3_1
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      I3 => I2_0_1,
      O => and23_O
    );
and3: component design_1_and_1_1
     port map (
      I0 => I1_0_2,
      I1 => I2_0_1,
      O => and3_O
    );
and4: component design_1_and_0_1
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      O => and4_O
    );
and5: component design_1_and_0_2
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I2_0_1,
      O => and5_O
    );
and6: component design_1_and_0_3
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      O => and6_O
    );
and7: component design_1_and_0_4
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      I2 => I1_0_2,
      I3 => I2_0_1,
      O => and7_O
    );
and8: component design_1_and_1_2
     port map (
      I0 => I1_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and8_O
    );
and9: component design_1_and_2_1
     port map (
      I0 => I0_0_1,
      I1 => I1_0_2,
      I2 => I2_0_1,
      O => and9_O
    );
gnd_0: component design_1_gnd_0_0
     port map (
      O => gnd_0_O
    );
or0: component design_1_or_0_0
     port map (
      I0 => xor1_O,
      I1 => and5_O,
      I2 => and6_O,
      O => or0_O
    );
or1: component design_1_or_0_1
     port map (
      I0 => and7_O,
      I1 => and8_O,
      I2 => and9_O,
      O => or1_O
    );
or2: component design_1_or_0_2
     port map (
      I0 => xor2_O,
      I1 => and12_O,
      I2 => and13_O,
      O => or2_O
    );
or3: component design_1_or_0_3
     port map (
      I0 => and14_O,
      I1 => and15_O,
      I2 => and16_O,
      O => or3_O
    );
or4: component design_1_or_0_4
     port map (
      I0 => and17_O,
      I1 => and18_O,
      I2 => and19_O,
      O => or4_O
    );
or5: component design_1_or_0_5
     port map (
      I0 => and20_O,
      I1 => and21_O,
      I2 => and22_O,
      I3 => and23_O,
      O => or5_O
    );
vcc_0: component design_1_vcc_0_0
     port map (
      O => vcc_0_O
    );
vcc_1: component design_1_vcc_0_1
     port map (
      O => vcc_1_O
    );
xor0: component design_1_xor_0_0
     port map (
      I0 => and0_O,
      I1 => and1_O,
      I2 => and2_O,
      O => xor0_O
    );
xor1: component design_1_xor_0_1
     port map (
      I0 => and3_O,
      I1 => and4_O,
      O => xor1_O
    );
xor2: component design_1_xor_0_2
     port map (
      I0 => and10_O,
      I1 => and11_O,
      O => xor2_O
    );
end STRUCTURE;
