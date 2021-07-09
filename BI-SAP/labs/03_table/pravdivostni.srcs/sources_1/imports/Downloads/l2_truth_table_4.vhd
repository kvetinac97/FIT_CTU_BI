-- potøebné knihovny pro práci
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

-- popis vstupù a výstupù funkce - vstupy a, b, c, d, výstup f
entity truth_table_4 is
    Port ( d : in std_logic;
           c : in std_logic;
           b : in std_logic;
           a : in std_logic;
           f : out std_logic);
end truth_table_4;

-- chování funkce - jako pamì? ROM, a, b, c, d je adresa, f jsou data
architecture Behavioral of truth_table_4 is

-- definice typu PRAVDIVOSTNÍ TABULKA - obsahuje 16 výstupù (2^4 = 16) typu standardní logika - nuly a jednièky
  type truth_table is array (0 to 15) of std_logic;

-- definice NAŠÍ FUNKCE f(d, c, b, a) pravdivostní tabulkou
  constant our_function : truth_table := truth_table'(
 -- ------------------------------------------------------------------------------
 -- |  funèkní hodnota f(d, c, b, a) |  stavový index s  |  d  |  c  |  b  |  a  | 
 -- ------------------------------------------------------------------------------
                  '1',            -- |           0       |  0  |  0  |  0  |  0  |
                  '0',            -- |           1       |  0  |  0  |  0  |  1  |
                  '1',            -- |           2       |  0  |  0  |  1  |  0  |
                  '-',            -- |           3       |  0  |  0  |  1  |  1  |
                  '0',            -- |           4       |  0  |  1  |  0  |  0  |
                  '0',            -- |           5       |  0  |  1  |  0  |  1  |
                  '-',            -- |           6       |  0  |  1  |  1  |  0  |
                  '1',            -- |           7       |  0  |  1  |  1  |  1  |
                  '1',            -- |           8       |  1  |  0  |  0  |  0  |
                  '1',            -- |           9       |  1  |  0  |  0  |  1  |
                  '0',            -- |          10       |  1  |  0  |  1  |  0  |
                  '-',            -- |          11       |  1  |  0  |  1  |  1  |
                  '0',            -- |          12       |  1  |  1  |  0  |  0  |
                  '0',            -- |          13       |  1  |  1  |  0  |  1  |
                  '0',            -- |          14       |  1  |  1  |  1  |  0  |
                  '0'             -- |          15       |  1  |  1  |  1  |  1  |
 -- ------------------------------------------------------------------------------
);

	signal address : std_logic_vector(3 downto 0);
begin
	address(3) <= d;
	address(2) <= c;
	address(1) <= b;
	address(0) <= a;

	f <= our_function(conv_integer(address(3 downto 0)));

end Behavioral;
