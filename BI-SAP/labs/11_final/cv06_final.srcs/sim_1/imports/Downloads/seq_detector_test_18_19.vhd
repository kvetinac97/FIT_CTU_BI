-- knihovna typù pro práci
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

-- prázdná entita zapouzdøující testovací sadu
ENTITY trezor_test IS
END trezor_test;

-- popis chování testovací sady
-- hlavièka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS 
ARCHITECTURE behavior OF trezor_test IS

-- definice vstupù a výstupù testované komponenty
  -- vstupy:  CLK, BTN, RESET, SWITCH 
  -- výstupy: Y
-- VSTUPY A VÝSTUPY KOMPONENTY (COMPONENT seq_detector) MUSÍ BÝT STEJNÉ JAKO POPISY VSTUPÙ A VÝSTUPÙ VE SCHÉMATU
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

   -- vstupní signály ovládané testovací sadou
   signal CLK : std_logic := '0';
   signal RESET : std_logic := '0';
   signal UP : std_logic := '0';
   signal DOWN : std_logic := '0';
   signal LEFT : std_logic := '0';
   signal RIGHT : std_logic := '0';

   -- výstupní signály ovládané testovací sadou
   signal Y    : std_logic;
    signal TB1    : std_logic;
     signal TB0    : std_logic;
      signal TS1    : std_logic;
       signal TS0    : std_logic;

   -- definice periody hodin
   constant CLK_period : time := 10 ns;
 
BEGIN
 
    -- mapování vstupù a výstupù testované komponenty na signály ovládané testovací sadou
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

   -- paralelní proces obsluhující funkci hodin
   CLK_process :process
   begin
        CLK <= '1';
        wait for CLK_period/2;
        CLK <= '0';
        wait for CLK_period/2;
   end process;
 

   -- simulaèní proces realizující chování testovací sady
   stim_proc: process
   begin        
      -- poèáteèní nastavení
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
      
      -- konec testu lze vyvoval pouze selháním
      report "Test dokoncen" severity FAILURE;

      wait;
   end process;

END;
