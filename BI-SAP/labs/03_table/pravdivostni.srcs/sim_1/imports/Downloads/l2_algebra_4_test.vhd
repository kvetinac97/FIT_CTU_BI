-- knihovna typ� pro pr�ci
LIBRARY IEEE;
USE IEEE.STD_LOGIC_1164.ALL;
USE IEEE.STD_LOGIC_ARITH.ALL;
USE IEEE.STD_LOGIC_UNSIGNED.ALL;
  
-- pr�zdn� entita zapouzd�uj�c� testovac� sadu
ENTITY algebra_4_test IS
END algebra_4_test;
 
-- popis chov�n� testovac� sady
-- hlavi�ka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS
ARCHITECTURE behavior OF algebra_4_test IS 
 
   -- definice vstup� a v�stup� testovan� komponenty
   -- vstupy:  a, b, c, d
   -- v�stupy: f
   -- POZOR!
   -- N�ZEV KOMPONENTY (COMPONENT <nazev>) MUS� B�T STEJN� JAKO N�ZEV TESTOVAN� ENTITY
   -- V TOMTO P��PAD�: COMPONENT algebra_4
   -- VSTUPY A V�STUPY KOMPONENTY (COMPONENT algebra_4) MUS� B�T STEJN� JAKO POPISY VSTUP� A V�STUP� TESTOVAN� ENTITY
   COMPONENT algebra_4
   PORT(
      d : IN  std_logic;
      c : IN  std_logic;
      b : IN  std_logic;
      a : IN  std_logic;
      f : OUT  std_logic
   );
   END COMPONENT;
    
   -- vstupn� sign�ly ovl�dan� testovac� sadou s po��te�n�m nastaven�m
   SIGNAL d : std_logic := '0';
   SIGNAL c : std_logic := '0';
   SIGNAL b : std_logic := '0';
   SIGNAL a : std_logic := '0';

   -- v�stupn� sign�ly ovl�dan� testovac� sadou
   SIGNAL f : std_logic;
 
BEGIN
 
   -- mapov�n� vstup� a v�stup� testovan� komponenty na sign�ly ovl�dan� testovac� sadou
   uut: algebra_4 PORT MAP (
      d => d,
      c => c,
      b => b,
      a => a,
      f => f
   );
 
   -- simula�n� proces realizuj�c� chov�n� testovac� sady
   stim_proc: PROCESS
   BEGIN		

      -- testov�n� v�ech mo�n�ch kombinac�
      -- 4 vstupy => 4 FOR-cykly => 2^4 mo�n�ch vstupn�ch kombinac�
      FOR i IN 0 TO 1 LOOP
         FOR j IN 0 TO 1 LOOP
            FOR k IN 0 TO 1 LOOP 
               FOR l IN 0 TO 1 LOOP
                  -- vstup se zm�n� za 20 nanosekund
                  wait FOR 20 ns;
                  a <= NOT(a);  		
               END LOOP;
               b <= NOT(b);
            END LOOP;
            c <= NOT(c);
         END LOOP;
         d <= NOT(d);
      END LOOP;
      
      -- assert pro ukon�en� simulace
      ASSERT FALSE
        REPORT " Simulace dokon�ena. "
          SEVERITY failure;									
		
   END PROCESS;

END;
