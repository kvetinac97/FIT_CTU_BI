-- knihovna typu pro praci
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.numeric_std.ALL;

-- definice vstupu a vystupu
entity next_state_logic is
  Port (
    q0    : in  std_logic;
    q1    : in  std_logic;
    q2    : in  std_logic;
    sw    : in  std_logic;
    d0    : out std_logic;
    d1    : out std_logic;
    d2    : out std_logic
  );
end next_state_logic;

-- vlastni popis chovani prechodovych funkci automatu
architecture Behavioral of next_state_logic is
begin
  -- ve vyrazech pouzivejte nasledujc operatory:
  -- ----------------------------------
  -- | opertor | priklad pouziti     |
  -- ----------------------------------
  -- |    and   |	a and b and c       |
  -- |    or    |	a or b or (c and d) |
  -- |    not   | not a               |
  -- ----------------------------------
  -- pouvejte zvorky!
  -- priklad:
  --   zadani:                            f(a, b, c) = a * #b * #c + a * b
  --   reprezentace na nasledujc radce: f <= (a and (not b) and (not c)) or (a and b)

    d0 <= ( (not sw) or ( q2 and (not q1) and q0 ) );
    d1 <= ( ( sw and (not q1) and q0 ) or ( sw and (not q2) and q0 ) or ( sw and (not q2) and q1 ) or ( (not q2) and q1 and q0 ) );
    d2 <= ( ( (not sw) and (not q2) and q0 ) or ( (not sw) and (not q1) and q0 ) or ( sw and q1 and (not q0) ) or ( sw and (not q2) and q1 ) );

end Behavioral;
