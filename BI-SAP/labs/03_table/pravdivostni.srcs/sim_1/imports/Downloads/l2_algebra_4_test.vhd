-- knihovna typù pro práci
LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
USE IEEE.STD_LOGIC_ARITH.ALL;
USE IEEE.STD_LOGIC_UNSIGNED.ALL;
  
-- prázdná entita zapouzdøující testovací sadu
ENTITY algebra_4_test IS
END algebra_4_test;
 
-- popis chování testovací sady
-- hlavièka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS
ARCHITECTURE behavior OF algebra_4_test IS 
 
   -- definice vstupù a výstupù testované komponenty
   -- vstupy:  a, b, c, d
   -- výstupy: f
   -- POZOR!
   -- NÁZEV KOMPONENTY (COMPONENT <nazev>) MUSÍ BÝT STEJNÝ JAKO NÁZEV TESTOVANÉ ENTITY
   -- V TOMTO PØÍPADÌ: COMPONENT algebra_4
   -- VSTUPY A VÝSTUPY KOMPONENTY (COMPONENT algebra_4) MUSÍ BÝT STEJNÉ JAKO POPISY VSTUPÙ A VÝSTUPÙ TESTOVANÉ ENTITY
   COMPONENT algebra_4
   PORT(
      d : IN  std_logic;
      c : IN  std_logic;
      b : IN  std_logic;
      a : IN  std_logic;
      f : OUT  std_logic
   );
   END COMPONENT;
    
   -- vstupní signály ovládané testovací sadou s poèáteèním nastavením
   SIGNAL d : std_logic := '0';
   SIGNAL c : std_logic := '0';
   SIGNAL b : std_logic := '0';
   SIGNAL a : std_logic := '0';

   -- výstupní signály ovládané testovací sadou
   SIGNAL f : std_logic;
 
BEGIN
 
   -- mapování vstupù a výstupù testované komponenty na signály ovládané testovací sadou
   uut: algebra_4 PORT MAP (
      d => d,
      c => c,
      b => b,
      a => a,
      f => f
   );
 
   -- simulaèní proces realizující chování testovací sady
   stim_proc: PROCESS
   BEGIN		

      -- testování všech možných kombinací
      -- 4 vstupy => 4 FOR-cykly => 2^4 možných vstupních kombinací
      FOR i IN 0 TO 1 LOOP
         FOR j IN 0 TO 1 LOOP
            FOR k IN 0 TO 1 LOOP 
               FOR l IN 0 TO 1 LOOP
                  -- vstup se zmìní za 20 nanosekund
                  wait FOR 20 ns;
                  a <= NOT(a);  		
               END LOOP;
               b <= NOT(b);
            END LOOP;
            c <= NOT(c);
         END LOOP;
         d <= NOT(d);
      END LOOP;
      
      -- assert pro ukonèení simulace
      ASSERT FALSE
        REPORT " Simulace dokonèena. "
          SEVERITY failure;									
		
   END PROCESS;

END;
