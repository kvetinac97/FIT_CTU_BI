--Copyright 1986-2018 Xilinx, Inc. All Rights Reserved.
----------------------------------------------------------------------------------
--Tool Version: Vivado v.2018.2.1 (win64) Build 2288692 Thu Jul 26 18:24:02 MDT 2018
--Date        : Wed Feb 19 18:17:55 2020
--Host        : PC-1042-103 running 64-bit major release  (build 9200)
--Command     : generate_target gates.bd
--Design      : gates
--Purpose     : IP block netlist
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
library UNISIM;
use UNISIM.VCOMPONENTS.ALL;
entity gates is
  port (
    A : in STD_LOGIC;
    B : in STD_LOGIC;
    C : in STD_LOGIC;
    D : in STD_LOGIC;
    E : in STD_LOGIC;
    F : in STD_LOGIC;
    G : in STD_LOGIC;
    H : in STD_LOGIC;
    P : out STD_LOGIC;
    Q : out STD_LOGIC;
    R : out STD_LOGIC;
    S : out STD_LOGIC
  );
  attribute CORE_GENERATION_INFO : string;
  attribute CORE_GENERATION_INFO of gates : entity is "gates,IP_Integrator,{x_ipVendor=xilinx.com,x_ipLibrary=BlockDiagram,x_ipName=gates,x_ipVersion=1.00.a,x_ipLanguage=VHDL,numBlks=4,numReposBlks=4,numNonXlnxBlks=4,numHierBlks=0,maxHierDepth=0,numSysgenBlks=0,numHlsBlks=0,numHdlrefBlks=0,numPkgbdBlks=0,bdsource=USER,synth_mode=OOC_per_IP}";
  attribute HW_HANDOFF : string;
  attribute HW_HANDOFF of gates : entity is "gates.hwdef";
end gates;

architecture STRUCTURE of gates is
  component gates_and_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component gates_and_0_0;
  component gates_and_1_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component gates_and_1_0;
  component gates_or_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component gates_or_0_0;
  component gates_xor_0_0 is
  port (
    I0 : in STD_LOGIC;
    I1 : in STD_LOGIC;
    O : out STD_LOGIC
  );
  end component gates_xor_0_0;
  signal I0_0_1 : STD_LOGIC;
  signal I0_1_1 : STD_LOGIC;
  signal I0_2_1 : STD_LOGIC;
  signal I0_3_1 : STD_LOGIC;
  signal I1_0_1 : STD_LOGIC;
  signal I1_1_1 : STD_LOGIC;
  signal I1_2_1 : STD_LOGIC;
  signal I1_3_1 : STD_LOGIC;
  signal and_0_O : STD_LOGIC;
  signal nand_0_O : STD_LOGIC;
  signal or_0_O : STD_LOGIC;
  signal xor_0_O : STD_LOGIC;
  attribute X_INTERFACE_INFO : string;
  attribute X_INTERFACE_INFO of A : signal is "xilinx.com:signal:data:1.0 DATA.A DATA";
  attribute X_INTERFACE_PARAMETER : string;
  attribute X_INTERFACE_PARAMETER of A : signal is "XIL_INTERFACENAME DATA.A, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of B : signal is "xilinx.com:signal:data:1.0 DATA.B DATA";
  attribute X_INTERFACE_PARAMETER of B : signal is "XIL_INTERFACENAME DATA.B, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of C : signal is "xilinx.com:signal:data:1.0 DATA.C DATA";
  attribute X_INTERFACE_PARAMETER of C : signal is "XIL_INTERFACENAME DATA.C, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of D : signal is "xilinx.com:signal:data:1.0 DATA.D DATA";
  attribute X_INTERFACE_PARAMETER of D : signal is "XIL_INTERFACENAME DATA.D, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of E : signal is "xilinx.com:signal:data:1.0 DATA.E DATA";
  attribute X_INTERFACE_PARAMETER of E : signal is "XIL_INTERFACENAME DATA.E, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of F : signal is "xilinx.com:signal:data:1.0 DATA.F DATA";
  attribute X_INTERFACE_PARAMETER of F : signal is "XIL_INTERFACENAME DATA.F, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of G : signal is "xilinx.com:signal:data:1.0 DATA.G DATA";
  attribute X_INTERFACE_PARAMETER of G : signal is "XIL_INTERFACENAME DATA.G, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of H : signal is "xilinx.com:signal:data:1.0 DATA.H DATA";
  attribute X_INTERFACE_PARAMETER of H : signal is "XIL_INTERFACENAME DATA.H, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of P : signal is "xilinx.com:signal:data:1.0 DATA.P DATA";
  attribute X_INTERFACE_PARAMETER of P : signal is "XIL_INTERFACENAME DATA.P, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of Q : signal is "xilinx.com:signal:data:1.0 DATA.Q DATA";
  attribute X_INTERFACE_PARAMETER of Q : signal is "XIL_INTERFACENAME DATA.Q, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of R : signal is "xilinx.com:signal:data:1.0 DATA.R DATA";
  attribute X_INTERFACE_PARAMETER of R : signal is "XIL_INTERFACENAME DATA.R, LAYERED_METADATA undef";
  attribute X_INTERFACE_INFO of S : signal is "xilinx.com:signal:data:1.0 DATA.S DATA";
  attribute X_INTERFACE_PARAMETER of S : signal is "XIL_INTERFACENAME DATA.S, LAYERED_METADATA undef";
begin
  I0_0_1 <= A;
  I0_1_1 <= E;
  I0_2_1 <= C;
  I0_3_1 <= G;
  I1_0_1 <= B;
  I1_1_1 <= F;
  I1_2_1 <= D;
  I1_3_1 <= H;
  P <= and_0_O;
  Q <= nand_0_O;
  R <= or_0_O;
  S <= xor_0_O;
and_0: component gates_and_0_0
     port map (
      I0 => I0_0_1,
      I1 => I1_0_1,
      O => and_0_O
    );
nand_0: component gates_and_1_0
     port map (
      I0 => I0_2_1,
      I1 => I1_2_1,
      O => nand_0_O
    );
or_0: component gates_or_0_0
     port map (
      I0 => I0_1_1,
      I1 => I1_1_1,
      O => or_0_O
    );
xor_0: component gates_xor_0_0
     port map (
      I0 => I0_3_1,
      I1 => I1_3_1,
      O => xor_0_O
    );
end STRUCTURE;
