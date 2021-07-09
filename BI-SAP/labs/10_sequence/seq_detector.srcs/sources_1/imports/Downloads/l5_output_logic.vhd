-- knihovna typu pro praci
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.numeric_std.ALL;

-- definice vstupu a vystupu
entity output_logic is
  Port (
    q0    : in  std_logic;	-- nemazat i kdyz jej nepouzijete (kvuli simulaci)
    q1    : in  std_logic;	-- nemazat i kdyz jej nepouzijete (kvuli simulaci)
    q2    : in  std_logic;	-- nemazat i kdyz jej nepouzijete (kvuli simulaci)
    sw    : in  std_logic;	-- nemazat i kdyz jej nepouzijete (kvuli simulaci)
    y     : out std_logic 	-- nemazat i kdyz jej nepouzijete (kvuli simulaci)
    
  );
end output_logic;

-- vlastni popis chovani vystupnich funkci automatu
architecture Behavioral of output_logic is
    
	signal q    : std_logic_vector(2 downto 0);
	
	
begin
  -- ve vyrazech pouzivejte nasledujici operatory:
  -- ----------------------------------
  -- | opertor |    priklad pouziti     |
  -- ----------------------------------
  -- |    and   |	a and b and c       |
  -- |    or    |	a or b or (c and d) |
  -- |    not   |   not a               |
  -- ----------------------------------
  -- pouvejte zvorky!
  -- priklad:
  --   zadani:                            f(a, b, c) = a * #b * #c + a * b
  --   reprezentace na nasledujc radce: f <= (a and (not b) and (not c)) or (a and b)
  
  y <= ( ( q2 and (not q1) ) xor ( q2 and q0 ) );
  
  
end Behavioral;
