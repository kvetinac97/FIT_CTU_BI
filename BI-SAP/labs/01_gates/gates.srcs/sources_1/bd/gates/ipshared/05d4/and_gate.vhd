----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 15.01.2018 18:38:11
-- Design Name: 
-- Module Name: and_gate - Behavioral
-- Project Name: 
-- Target Devices: 
-- Tool Versions: 
-- Description: 
-- 
-- Dependencies: 
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
-- 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity and_gate is
    Generic (I0_inverted: boolean := false;
             I1_inverted: boolean := false;
             I2_inverted: boolean := false;
             I3_inverted: boolean := false;
             I4_inverted: boolean := false;
             I5_inverted: boolean := false;
             I6_inverted: boolean := false;
             I7_inverted: boolean := false;
             O_inverted: boolean := false
            );
    Port (I0, I1, I2, I3, I4, I5, I6, I7: in STD_LOGIC;
          O: out STD_LOGIC
         );
end and_gate;

architecture Behavioral of and_gate is
signal I0_int, I1_int, I2_int, I3_int, I4_int, I5_int, I6_int, I7_int, O_int: STD_LOGIC;
begin
    O_int <= I0_int and I1_int and I2_int and I3_int and I4_int and I5_int and I6_int and I7_int;
    
    -- negovani vstupu a vystupu

    I0_neg: if (I0_inverted) generate
    begin
      I0_int <= not I0;
    end generate;
    I0_pos: if (not I0_inverted) generate
    begin
      I0_int <= I0;
    end generate;

    I1_neg: if (I1_inverted) generate
    begin
      I1_int <= not I1;
    end generate;
    I1_pos: if (not I1_inverted) generate
    begin
      I1_int <= I1;
    end generate;

    I2_neg: if (I2_inverted) generate
    begin
      I2_int <= not I2;
    end generate;
    I2_pos: if (not I2_inverted) generate
    begin
      I2_int <= I2;
    end generate;

    I3_neg: if (I3_inverted) generate
    begin
      I3_int <= not I3;
    end generate;
    I3_pos: if (not I3_inverted) generate
    begin
      I3_int <= I3;
    end generate;

    I4_neg: if (I4_inverted) generate
    begin
      I4_int <= not I4;
    end generate;
    I4_pos: if (not I4_inverted) generate
    begin
      I4_int <= I4;
    end generate;

    I5_neg: if (I5_inverted) generate
    begin
      I5_int <= not I5;
    end generate;
    I5_pos: if (not I5_inverted) generate
    begin
      I5_int <= I5;
    end generate;

    I6_neg: if (I6_inverted) generate
    begin
      I6_int <= not I6;
    end generate;
    I6_pos: if (not I6_inverted) generate
    begin
      I6_int <= I6;
    end generate;

    I7_neg: if (I7_inverted) generate
    begin
      I7_int <= not I7;
    end generate;
    I7_pos: if (not I7_inverted) generate
    begin
      I7_int <= I7;
    end generate;

    O_neg: if (O_inverted) generate
    begin
      O <= not O_int;
    end generate;
    O_pos: if (not O_inverted) generate
    begin
      O <= O_int;
    end generate;


end Behavioral;
