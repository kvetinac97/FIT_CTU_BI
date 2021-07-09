-- knihovna typu pro praci
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

-- definice vstupu a vystupu
entity output_logic is
  Port (	-- Do not delete any port even if you do not use it // nemazte zadny port i kdyz jej nepouzijete (kvuli simulaci)
    q0    : in  std_logic;
    q1    : in  std_logic;
    q2    : in  std_logic;
    INC   : in  std_logic;
    DIR   : in  std_logic;
    y0    : out std_logic;
    y1    : out std_logic;
    y2    : out std_logic
  );	-- Do not delete any port even if you do not use it // nemazte zadny port i kdyz jej nepouzijete (kvuli simulaci)
end output_logic;

-- vlastni popis chovani vystupnich funkci automatu
architecture Behavioral of output_logic is
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
  
  y2 <= q2; -- Keep connected to '0' if you do not use it // pokud tento vystup nevyuzijete, nechte jej pripojeny k log. 0 (kvuli simulaci)
  y1 <= ( q2 or q1 ); -- Keep connected to '0' if you do not use it // pokud tento vystup nevyuzijete, nechte jej pripojeny k log. 0 (kvuli simulaci)
  y0 <= ( q1 xor q0 ); -- Keep connected to '0' if you do not use it // pokud tento vystup nevyuzijete, nechte jej pripojeny k log. 0 (kvuli simulaci)

end Behavioral;
