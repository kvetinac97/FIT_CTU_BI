-- knihovna typů pro práci
library STD;
use STD.textio.all;

LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
USE IEEE.STD_LOGIC_ARITH.ALL;
USE IEEE.STD_LOGIC_UNSIGNED.ALL;
use IEEE.numeric_std.all;
use IEEE.std_logic_textio.all;

-- prazdna entita zapouzdrujici testovaci sadu
ENTITY multiplier_test IS
END multiplier_test;
 
-- popis chovani testovaci sady
-- hlavicka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS
ARCHITECTURE behavior OF multiplier_test IS 
 
   -- definice vstupu a vystupu testovane komponenty
   -- vstupy:  a1, a0, b1, b0
   -- vystupy: y3, y2, y1, y0
   -- POZOR!
   -- NAZEV KOMPONENTY (COMPONENT <nazev>) MUSI BYT STEJNY JAKO NAZEV TESTOVANE ENTITY
   -- V TOMTO PRIPADE: COMPONENT multiplier
   -- VSTUPY A VYSTUPY KOMPONENTY (COMPONENT multiplier) MUSI BYT STEJNE JAKO POPISY VSTUPU A VYSTUPU TESTOVANE ENTITY
   COMPONENT multiplier
   PORT(
      a1, a0, b1, b0 : IN  std_logic;
      y3, y2, y1, y0 : OUT  std_logic
   );
   END COMPONENT;
    
   -- vstupni signaly ovladane testovaci sadou s pocatecnim nastavenim
   SIGNAL a1 : std_logic := '0';
   SIGNAL a0 : std_logic := '0';
   SIGNAL b1 : std_logic := '0';
   SIGNAL b0 : std_logic := '0';

   -- vystupni signaly ovladane testovaci sadou
   SIGNAL y3, y2, y1, y0 : std_logic;
   
   signal y : std_logic_vector(3 downto 0);
   signal a : std_logic_vector(1 downto 0);
   signal b : std_logic_vector(1 downto 0);
 
BEGIN
 
--    mapovani vstupu a vystupu testovane komponenty na signaly ovladane testovaci sadou
   uut: multiplier PORT MAP (
     a1 => a1,
     a0 => a0,
     b1 => b1,
     b0 => b0,
     y3 => y3,
     y2 => y2,
     y1 => y1,
     y0 => y0
   );
 
   -- simulacni proces realizujici chovani testovaci sady
   stim_proc: PROCESS
      variable file_line    : line;
      variable i_vector     : std_logic_vector(3 downto 0);
   BEGIN		
      write(file_line, string'("### Simulation start ###"));
      writeline(output, file_line);
      write(file_line, string'("Index: B * A = Y"));
      writeline(output, file_line);
      -- testovani vsech moznych kombinaci
      -- 4 vstupy => 4 FOR-cykly => 2^4 moznych vstupnich kombinaci
      FOR i IN 0 TO 15 LOOP
         i_vector := std_logic_vector(to_unsigned(i, i_vector'length));
         a0 <= i_Vector(0);
         a1 <= i_Vector(1);
         b0 <= i_Vector(2);
         b1 <= i_Vector(3);
         wait for 10 ns;
         write(file_line, i);
         write(file_line, string'(": "));
         write(file_line, i_vector(3 downto 2));
         write(file_line, string'(" * "));
         write(file_line, i_vector(1 downto 0));
         write(file_line, string'(" = "));
         i_vector := y3 & y2 & y1 & y0;
         write(file_line, i_vector);
         writeline(output, file_line);
         wait for 10 ns;
      END LOOP;

      write(file_line, string'("### Simulation finished ###"));
      writeline(output, file_line);
      
      wait;
      
		
   END PROCESS;

END;