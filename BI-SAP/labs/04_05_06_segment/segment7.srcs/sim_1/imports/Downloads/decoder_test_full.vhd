-- knihovna typ� pro pr�ci
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
 
-- pr�zdn� entita zapouzd�uj�c� testovac� sadu
ENTITY decoder_7seg_test IS
END decoder_7seg_test;

-- popis chov�n� testovac� sady
-- hlavi�ka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS 
ARCHITECTURE behavior OF decoder_7seg_test IS 
 
-- definice vstup� a v�stup� testovan� komponenty
  -- vstupy:  D, C, B, A 
  -- v�stupy: f_a, f_b, f_c, f_d, f_e, f_f, f_g
-- POZOR!
   -- N�ZEV KOMPONENTY (COMPONENT <nazev>) MUS� B�T STEJN� JAKO N�ZEV TESTOVAN�HO SCH�MATU 
-- V TOMTO P��PAD�: COMPONENT decoder_7seg
-- VSTUPY A V�STUPY KOMPONENTY (COMPONENT decoder_7seg) MUS� B�T STEJN� JAKO POPISY VSTUP� A V�STUP� VE SCH�MATU
   COMPONENT decoder_7seg
   PORT(
      d : IN  std_logic;
      c : IN  std_logic;
      b : IN  std_logic;
      a : IN  std_logic;
      f_a : out  STD_LOGIC;
      f_b : out  STD_LOGIC;
      f_c : out  STD_LOGIC;
      f_d : out  STD_LOGIC;
      f_e : out  STD_LOGIC;
      f_f : out  STD_LOGIC;
      f_g : out  STD_LOGIC
   );
   END COMPONENT;
    
   -- vstupn� sign�ly ovl�dan� testovac� sadou
   signal d : std_logic := '0';
   signal c : std_logic := '0';
   signal b : std_logic := '0';
   signal a : std_logic := '0';

   -- v�stupn� sign�ly ovl�dan� testovac� sadou
   signal f_a : std_logic;
   signal f_b : std_logic;
   signal f_c : std_logic;
   signal f_d : std_logic;
   signal f_e : std_logic;
   signal f_f : std_logic;
   signal f_g : std_logic;
 
BEGIN
 
   -- mapov�n� vstup� a v�stup� testovan� komponenty na sign�ly ovl�dan� testovac� sadou
   uut: decoder_7seg PORT MAP (
      d => d,
      c => c,
      b => b,
      a => a,
      f_a => f_a,
      f_b => f_b,
      f_c => f_c,
      f_d => f_d,
      f_e => f_e,
      f_f => f_f,
      f_g => f_g
   );

   -- simula�n� proces realizuj�c� chov�n� testovac� sady
   stim_proc: process
   begin		
      -- testovani zobrazeni cisla 0
      d   <= '0';
      c   <= '0';
      b   <= '0';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='0' and f_d='0' and f_e='0' and f_f='0' and f_g='1'                      
      report "chyba pri zobrazeni 0"                                                                 
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 1
      d   <= '0';
      c   <= '0';
      b   <= '0';
      a   <= '1';
      wait for 5 ns;
      assert f_a='1' and f_b='0' and f_c='0' and f_d='1' and f_e='1' and f_f='1' and f_g='1'                
      report "chyba pri zobrazeni 1"                                                                 
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 2
      d   <= '0';
      c   <= '0';
      b   <= '1';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='1' and f_d='0' and f_e='0' and f_f='1' and f_g='0'                
      report "chyba pri zobrazeni 2"                                                                 
      severity ERROR;
		
      wait for 20 ns;		

      -- testovani zobrazeni cisla 3
      d   <= '0';
      c   <= '0';
      b   <= '1';
      a   <= '1';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='0' and f_d='0' and f_e='1' and f_f='1' and f_g='0'                
      report "chyba pri zobrazeni 3"                                                                 
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 4		
      d   <= '0';
      c   <= '1';
      b   <= '0';
      a   <= '0';
      wait for 5 ns;
      assert f_a='1' and f_b='0' and f_c='0' and f_d='1' and f_e='1' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni 4"                                                                 
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 5		
      d   <= '0';
      c   <= '1';
      b   <= '0';
      a   <= '1';
      wait for 5 ns;
      assert f_a='0' and f_b='1' and f_c='0' and f_d='0' and f_e='1' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni 5"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 6		
      d   <= '0';
      c   <= '1';
      b   <= '1';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='1' and f_c='0' and f_d='0' and f_e='0' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni 6"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 7		
      d   <= '0';
      c   <= '1';
      b   <= '1';
      a   <= '1';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='0' and f_d='1' and f_e='1' and f_f='1' and f_g='1'                
      report "chyba pri zobrazeni 7"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 8
      d   <= '1';
      c   <= '0';
      b   <= '0';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='0' and f_d='0' and f_e='0' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni 8"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla 9		
      d   <= '1';
      c   <= '0';
      b   <= '0';
      a   <= '1';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='0' and f_d='0' and f_e='1' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni 9"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla A		
      d   <= '1';
      c   <= '0';
      b   <= '1';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='0' and f_c='0' and f_d='1' and f_e='0' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni A"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla B		
      d   <= '1';
      c   <= '0';
      b   <= '1';
      a   <= '1';
      wait for 5 ns;
      assert f_a='1' and f_b='1' and f_c='0' and f_d='0' and f_e='0' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni B"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla C		
      d   <= '1';
      c   <= '1';
      b   <= '0';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='1' and f_c='1' and f_d='0' and f_e='0' and f_f='0' and f_g='1'                
      report "chyba pri zobrazeni C"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla D		
      d   <= '1';
      c   <= '1';
      b   <= '0';
      a   <= '1';
      wait for 5 ns;
      assert f_a='1' and f_b='0' and f_c='0' and f_d='0' and f_e='0' and f_f='1' and f_g='0'                
      report "chyba pri zobrazeni D"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla E		
      d   <= '1';
      c   <= '1';
      b   <= '1';
      a   <= '0';
      wait for 5 ns;
      assert f_a='0' and f_b='1' and f_c='1' and f_d='0' and f_e='0' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni E"
      severity ERROR;
		
      wait for 20 ns;

      -- testovani zobrazeni cisla F		
      d   <= '1';
      c   <= '1';
      b   <= '1';
      a   <= '1';
      wait for 5 ns;
      assert f_a='0' and f_b='1' and f_c='1' and f_d='1' and f_e='0' and f_f='0' and f_g='0'                
      report "chyba pri zobrazeni F"
      severity ERROR;
		
      wait for 20 ns;
				
      wait;
   end process;

END;
