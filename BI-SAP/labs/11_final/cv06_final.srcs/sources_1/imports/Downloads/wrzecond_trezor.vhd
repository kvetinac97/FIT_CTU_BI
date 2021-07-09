-- knihovna typů pro práci
library STD;
use STD.textio.all;

LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
USE IEEE.STD_LOGIC_ARITH.ALL;
USE IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.numeric_std.all;
use IEEE.std_logic_textio.all;

entity next_state_logic is
    Port ( buttons1 : in STD_LOGIC;
           buttons0 : in STD_LOGIC;
           q1 : in STD_LOGIC;
           q0 : in STD_LOGIC;
           d1 : out STD_LOGIC;
           d0 : out STD_LOGIC);
end next_state_logic;
 
architecture Behavioral of next_state_logic is
begin
  
  d0 <= ( ( q1 and q0 ) or ( (not buttons1) and buttons0 and (not q1) and (not q0) ) or ( buttons1 and (not buttons0) and q1 ) );
  d1 <= ( ( q1 and q0 ) or ( buttons1 and buttons0 and q0 ) or ( buttons1 and (not buttons0) and q1 ) );

end Behavioral;