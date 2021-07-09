-- knihovna typ� pro pr�ci
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

-- pr�zdn� entita zapouzd�uj�c� testovac� sadu
ENTITY trezor_test IS
END trezor_test;

-- popis chov�n� testovac� sady
-- hlavi�ka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS 
ARCHITECTURE behavior OF trezor_test IS

-- definice vstup� a v�stup� testovan� komponenty
  -- vstupy:  CLK, BTN, RESET, SWITCH 
  -- v�stupy: Y
-- VSTUPY A V�STUPY KOMPONENTY (COMPONENT seq_detector) MUS� B�T STEJN� JAKO POPISY VSTUP� A V�STUP� VE SCH�MATU
   COMPONENT trezor_wrapper
   PORT(
     CLK    : IN  std_logic;
     UP     : IN  std_logic;
     RIGHT     : IN  std_logic;
     DOWN     : IN  std_logic;
     LEFT     : IN  std_logic;
     RESET  : IN  std_logic;
     Y : OUT std_logic;
     TS1 : OUT std_logic;
     TS0 : OUT std_logic;
     TB1 : OUT std_logic;
     TB0 : OUT std_logic
   );
   END COMPONENT;

   -- vstupn� sign�ly ovl�dan� testovac� sadou
   signal CLK : std_logic := '0';
   signal RESET : std_logic := '0';
   signal UP : std_logic := '0';
   signal DOWN : std_logic := '0';
   signal LEFT : std_logic := '0';
   signal RIGHT : std_logic := '0';

   -- v�stupn� sign�ly ovl�dan� testovac� sadou
   signal Y    : std_logic;
    signal TB1    : std_logic;
     signal TB0    : std_logic;
      signal TS1    : std_logic;
       signal TS0    : std_logic;

   -- definice periody hodin
   constant CLK_period : time := 10 ns;
 
BEGIN
 
    -- mapov�n� vstup� a v�stup� testovan� komponenty na sign�ly ovl�dan� testovac� sadou
   uut: trezor_wrapper PORT MAP (
          CLK       => CLK,
          RESET     => RESET,
          UP    => UP,
          DOWN    => DOWN,
          LEFT    => LEFT,
          RIGHT    => RIGHT,
          Y    => Y,
          TS1 => TS1,
          TS0 => TS0,
          TB1 => TB1,
          TB0 => TB0
        );

   -- paraleln� proces obsluhuj�c� funkci hodin
   CLK_process :process
   begin
        CLK <= '1';
        wait for CLK_period/2;
        CLK <= '0';
        wait for CLK_period/2;
   end process;
 

   -- simula�n� proces realizuj�c� chov�n� testovac� sady
   stim_proc: process
   begin        
      -- po��te�n� nastaven�
      RESET    <= '1';
      UP <= '0';
      DOWN <= '0';
      LEFT <= '0';
      RIGHT <= '0';
      wait for 100 ns;  

      report "Testovani dobre posloupnosti trezoru:"   
      severity NOTE;
      RESET    <= '0';
      UP <= '0';
      DOWN <= '0';
      LEFT <= '0';
      RIGHT <= '1';
      
      wait for CLK_period/2;
      assert Y='0'
      report "detekce prvni casti neprosla"
      severity ERROR;
      wait for CLK_period/2;
      
      wait for 100 ns;
      
      -- konec testu lze vyvoval pouze selh�n�m
      report "Test dokoncen" severity FAILURE;

      wait;
   end process;

END;
