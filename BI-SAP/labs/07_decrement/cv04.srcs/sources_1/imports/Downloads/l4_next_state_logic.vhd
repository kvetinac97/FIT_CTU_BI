-- knihovna typu pro praci
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

-- definice vstupu a vystupu
entity next_state_logic is
  Port (	-- Do not delete any port even if you do not use it // nemazte zadny port i kdyz jej nepouzijete (kvuli simulaci)
    q0    : in  std_logic;
    q1    : in  std_logic;
    q2    : in  std_logic;
    INC   : in  std_logic;
    DIR   : in  std_logic;
    d0    : out std_logic;
    d1    : out std_logic;
    d2    : out std_logic
  );	-- Do not delete any port even if you do not use it // nemazte zadny port i kdyz jej nepouzijete (kvuli simulaci)
end next_state_logic;

-- vlastni popis chovani prechodovych funkci automatu
architecture Behavioral of next_state_logic is
begin
  -- ve vyrazech pouzivejte nasledujici operatory:
  -- ----------------------------------
  -- | operator | priklad pouziti     |
  -- ----------------------------------
  -- |    and   |	a and b and c       |
  -- |    or    |	a or b or (c and d) |
  -- |    not   | not a               |
  -- ----------------------------------
  -- pouzivejte zavorky!
  -- priklad:
  --   zadani:                            f(a, b, c) = a * #b * #c + a * b
  --   reprezentace na nasledujc radce: f <= (a and (not b) and (not c)) or (a and b)
  
  d2 <= ( ( (not INC) and q2 ) or ( q2 and (not q0) ) or ( INC and q1 and q0 ) ); -- Keep connected to '0' if you do not use it // pokud tento vystup nevyuzijete, nechte jej pripojeny k log. 0 (kvuli simulaci)
  d1 <= ( ( q1 and (not q0) ) or ( (not INC) and q1) or ( INC and (not q2) and (not q1) and q0 ) ); -- Keep connected to '0' if you do not use it // pokud tento vystup nevyuzijete, nechte jej pripojeny k log. 0 (kvuli simulaci)
  d0 <= ( INC xor q0 ); -- Keep connected to '0' if you do not use it // pokud tento vystup nevyuzijete, nechte jej pripojeny k log. 0 (kvuli simulaci)

end Behavioral;
