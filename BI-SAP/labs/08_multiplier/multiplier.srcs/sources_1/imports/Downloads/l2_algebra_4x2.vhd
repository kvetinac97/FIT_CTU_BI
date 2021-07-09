-- knihovna typ pro prci
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

-- definice vstup a vstup - vstupy a, b, c, d, vstupy f
entity multiplier is
    Port ( b1 : in std_logic;
           b0 : in std_logic;
           a1 : in std_logic;
           a0 : in std_logic;
           y3, y2, y1, y0 : out std_logic);
end multiplier;

-- vlastn popis chovn obvodu -> vpoet funkce f
architecture Behavioral of multiplier is
begin
  -- ve vrazech pouivejte nsledujc opertory:
  -- ----------------------------------
  -- | opertor | pklad pouit     |
  -- ----------------------------------
  -- |    and   |	a and b and c       |
  -- |    or    |	a or b or (c and d) |
  -- |    not   | not a               |
  -- ----------------------------------
  -- pouvejte zvorky!
  -- pklad:
  --   zadn:                            f(a, b, c) = a * #b * #c + a * b
  --   reprezentace na nsledujc dce: f <= (a and (not b) and (not c)) or (a and b)
  -- jin vzorov pklad:
  y3 <= ( b1 and b0 and a1 and a0 );
  y2 <= ( ( b1 and a1 and (not a0) ) or ( b1 and a1 and (not b0) ) );
  y1 <= ( ( b1 and a0 ) xor ( a1 and b0 ) );
  y0 <= ( b0 and a0 );
end Behavioral;
