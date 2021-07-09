--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
--Date        : Tue Feb 25 08:24:06 2020
--Host        : PC-1042-103 running 64-bit major release  (build 9200)
--Command     : generate_target combi.bd
--Design      : combi
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity combi is
  port (
    a : in STD_LOGIC;
    b : in STD_LOGIC;
    c : in STD_LOGIC;
    d : in STD_LOGIC;
    s : out STD_LOGIC
  );
  attribute CORE_GENERATION_INFO : string;
  attribute CORE_GENERATION_INFO of combi : entity is "combi,IP_Integrator,{x_ipVendor=xilinx.com,x_ipLibrary=BlockDiagram,x_ipName=combi,x_ipVersion=1.00.a,x_ipLanguage=VHDL,numBlks=4,numReposBlks=4,numNonXlnxBlks=4,numHierBlks=0,maxHierDepth=0,numSysgenBlks=0,numHlsBlks=0,numHdlrefBlks=0,numPkgbdBlks=0,bdsource=USER,synth_mode=OOC_per_IP}";
  attribute HW_HANDOFF : string;
  attribute HW_HANDOFF of combi : entity is "combi.hwdef";
end combi;

architecture STRUCTURE of combi is
  component combi_and_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component combi_and_0_0;
  component combi_and_0_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component combi_and_0_1;
  component combi_and_0_2 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component combi_and_0_2;
  component combi_or_0_1 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    I2 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component combi_or_0_1;
  signal I0_0_1 : STD_LOGIC;
  signal I0_0_2 : STD_LOGIC;
  signal I1_0_1 : STD_LOGIC;
  signal I1_0_2 : STD_LOGIC;
  signal and0_O : STD_LOGIC;
  signal and1_O : STD_LOGIC;
  signal and2_O : STD_LOGIC;
  signal or0_O : STD_LOGIC;
  attribute X_INTERFACE_INFO : string;
  attribute X_INTERFACE_INFO of a : signal is "xilinx.com:signal:data:1.0 DATA.A DATA";
  attribute X_INTERFACE_PARAMETER : string;
  attribute X_INTERFACE_PARAMETER of a : signal is "XIL_INTERFACENAME DATA.A, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of b : signal is "xilinx.com:signal:data:1.0 DATA.B DATA";
  attribute X_INTERFACE_PARAMETER of b : signal is "XIL_INTERFACENAME DATA.B, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of c : signal is "xilinx.com:signal:data:1.0 DATA.C DATA";
  attribute X_INTERFACE_PARAMETER of c : signal is "XIL_INTERFACENAME DATA.C, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of d : signal is "xilinx.com:signal:data:1.0 DATA.D DATA";
  attribute X_INTERFACE_PARAMETER of d : signal is "XIL_INTERFACENAME DATA.D, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of s : signal is "xilinx.com:signal:data:1.0 DATA.S DATA";
  attribute X_INTERFACE_PARAMETER of s : signal is "XIL_INTERFACENAME DATA.S, LAYERED_METADATA undef";
begin
  I0_0_1 <= b;
  I0_0_2 <= a;
  I1_0_1 <= d;
  I1_0_2 <= c;
  s <= or0_O;
and0: component combi_and_0_0
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      O => and0_O
    );
and1: component combi_and_0_1
     port map (
      I0 => I0_0_1,
      I1 => I1_0_2,
      I2 => I1_0_1,
      O => and1_O
    );
and2: component combi_and_0_2
     port map (
      I0 => I0_0_2,
      I1 => I0_0_1,
      I2 => I1_0_2,
      O => and2_O
    );
or0: component combi_or_0_1
     port map (
      I0 => and0_O,
      I1 => and1_O,
      I2 => and2_O,
      O => or0_O
    );
end STRUCTURE;
