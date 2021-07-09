-- (c) Copyright 1995-2020 Xilinx, Inc. All rights reserved.
-- 
-- This file contains confidential and proprietary information
-- of Xilinx, Inc. and is protected under U.S. and
-- international copyright and other intellectual property
-- laws.
-- 
-- DISCLAIMER
-- This disclaimer is not a license and does not grant any
-- rights to the materials distributed herewith. Except as
-- otherwise provided in a valid license issued to you by
-- Xilinx, and to the maximum extent permitted by applicable
-- law: (1) THESE MATERIALS ARE MADE AVAILABLE "AS IS" AND
-- WITH ALL FAULTS, AND XILINX HEREBY DISCLAIMS ALL WARRANTIES
-- AND CONDITIONS, EXPRESS, IMPLIED, OR STATUTORY, INCLUDING
-- BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY, NON-
-- INFRINGEMENT, OR FITNESS FOR ANY PARTICULAR PURPOSE; and
-- (2) Xilinx shall not be liable (whether in contract or tort,
-- including negligence, or under any other theory of
-- liability) for any loss or damage of any kind or nature
-- related to, arising under or in connection with these
-- materials, including for any direct, or any indirect,
-- special, incidental, or consequential loss or damage
-- (including loss of data, profits, goodwill, or any type of
-- loss or damage suffered as a result of any action brought
-- by a third party) even if such damage or loss was
-- reasonably foreseeable or Xilinx had been advised of the
-- possibility of the same.
-- 
-- CRITICAL APPLICATIONS
-- Xilinx products are not designed or intended to be fail-
-- safe, or for use in any application requiring fail-safe
-- performance, such as life-support or safety devices or
-- systems, Class III medical devices, nuclear facilities,
-- applications related to the deployment of airbags, or any
-- other applications that could lead to death, personal
-- injury, or severe property or environmental damage
-- (individually and collectively, "Critical
-- Applications"). Customer assumes the sole risk and
-- liability of any use of Xilinx products in Critical
-- Applications, subject only to applicable laws and
-- regulations governing limitations on product liability.
-- 
-- THIS COPYRIGHT NOTICE AND DISCLAIMER MUST BE RETAINED AS
-- PART OF THIS FILE AT ALL TIMES.
-- 
-- DO NOT MODIFY THIS FILE.

-- IP VLNV: FIT:user:and:1.0
-- IP Revision: 17

LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
USE ieee.numeric_std.ALL;

ENTITY design_1_and_1_5 IS
  PORT (
    I0 : IN STD_LOGIC;
    I1 : IN STD_LOGIC;
    I2 : IN STD_LOGIC;
    I3 : IN STD_LOGIC;
    O : OUT STD_LOGIC
  );
END design_1_and_1_5;

ARCHITECTURE design_1_and_1_5_arch OF design_1_and_1_5 IS
  ATTRIBUTE DowngradeIPIdentifiedWarnings : STRING;
  ATTRIBUTE DowngradeIPIdentifiedWarnings OF design_1_and_1_5_arch: ARCHITECTURE IS "yes";
  COMPONENT and_gate IS
    GENERIC (
      I0_inverted : BOOLEAN;
      I1_inverted : BOOLEAN;
      I2_inverted : BOOLEAN;
      I3_inverted : BOOLEAN;
      I4_inverted : BOOLEAN;
      I5_inverted : BOOLEAN;
      I6_inverted : BOOLEAN;
      I7_inverted : BOOLEAN;
      O_inverted : BOOLEAN
    );
    PORT (
      I0 : IN STD_LOGIC;
      I1 : IN STD_LOGIC;
      I2 : IN STD_LOGIC;
      I3 : IN STD_LOGIC;
      I4 : IN STD_LOGIC;
      I5 : IN STD_LOGIC;
      I6 : IN STD_LOGIC;
      I7 : IN STD_LOGIC;
      O : OUT STD_LOGIC
    );
  END COMPONENT and_gate;
  ATTRIBUTE X_CORE_INFO : STRING;
  ATTRIBUTE X_CORE_INFO OF design_1_and_1_5_arch: ARCHITECTURE IS "and_gate,Vivado 2018.2.1";
  ATTRIBUTE CHECK_LICENSE_TYPE : STRING;
  ATTRIBUTE CHECK_LICENSE_TYPE OF design_1_and_1_5_arch : ARCHITECTURE IS "design_1_and_1_5,and_gate,{}";
  ATTRIBUTE IP_DEFINITION_SOURCE : STRING;
  ATTRIBUTE IP_DEFINITION_SOURCE OF design_1_and_1_5_arch: ARCHITECTURE IS "package_project";
  ATTRIBUTE X_INTERFACE_INFO : STRING;
  ATTRIBUTE X_INTERFACE_PARAMETER : STRING;
  ATTRIBUTE X_INTERFACE_PARAMETER OF O: SIGNAL IS "XIL_INTERFACENAME O, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  ATTRIBUTE X_INTERFACE_INFO OF O: SIGNAL IS "xilinx.com:signal:data:1.0 O DATA";
  ATTRIBUTE X_INTERFACE_PARAMETER OF I3: SIGNAL IS "XIL_INTERFACENAME I3, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  ATTRIBUTE X_INTERFACE_INFO OF I3: SIGNAL IS "xilinx.com:signal:data:1.0 I3 DATA";
  ATTRIBUTE X_INTERFACE_PARAMETER OF I2: SIGNAL IS "XIL_INTERFACENAME I2, POLARITY ACTIVE_HIGH, LAYERED_METADATA undef";
  ATTRIBUTE X_INTERFACE_INFO OF I2: SIGNAL IS "xilinx.com:signal:data:1.0 I2 DATA";
  ATTRIBUTE X_INTERFACE_PARAMETER OF I1: SIGNAL IS "XIL_INTERFACENAME I1, POLARITY ACTIVE_LOW, LAYERED_METADATA undef";
  ATTRIBUTE X_INTERFACE_INFO OF I1: SIGNAL IS "xilinx.com:signal:data:1.0 I1 DATA";
  ATTRIBUTE X_INTERFACE_PARAMETER OF I0: SIGNAL IS "XIL_INTERFACENAME I0, POLARITY ACTIVE_LOW, LAYERED_METADATA undef";
  ATTRIBUTE X_INTERFACE_INFO OF I0: SIGNAL IS "xilinx.com:signal:data:1.0 I0 DATA";
BEGIN
  U0 : and_gate
    GENERIC MAP (
      I0_inverted => true,
      I1_inverted => true,
      I2_inverted => false,
      I3_inverted => false,
      I4_inverted => false,
      I5_inverted => false,
      I6_inverted => false,
      I7_inverted => false,
      O_inverted => false
    )
    PORT MAP (
      I0 => I0,
      I1 => I1,
      I2 => I2,
      I3 => I3,
      I4 => '1',
      I5 => '1',
      I6 => '1',
      I7 => '1',
      O => O
    );
END design_1_and_1_5_arch;
