-- knihovna typù pro práci
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

-- prázdná entita zapouzdøující testovací sadu
ENTITY seq_detector_test IS
END seq_detector_test;

-- popis chování testovací sady
-- hlavièka ve formatu: ARCHITECTURE <libovolny_nazev> OF <nazev_entity> IS 
ARCHITECTURE behavior OF seq_detector_test IS 

-- definice vstupù a výstupù testované komponenty
  -- vstupy:  CLK, BTN, RESET, SWITCH 
  -- výstupy: DETECT
-- POZOR!
   -- NÁZEV KOMPONENTY (COMPONENT <nazev>) MUSÍ BÝT STEJNÝ JAKO NÁZEV TESTOVANÉHO SCHÉMATU 
-- V TOMTO PØÍPADÌ: COMPONENT seq_detector
-- VSTUPY A VÝSTUPY KOMPONENTY (COMPONENT seq_detector) MUSÍ BÝT STEJNÉ JAKO POPISY VSTUPÙ A VÝSTUPÙ VE SCHÉMATU
   COMPONENT seq_detector_wrapper
   PORT(
     CLK    : IN  std_logic;
     BTN    : IN  std_logic;
     RESET  : IN  std_logic;
     CE     : IN  std_logic;
     SWITCH : IN  std_logic;
     DETECT : OUT std_logic;
     S0    : OUT std_logic;
     S1    : OUT std_logic;
     S2    : OUT std_logic
   );
   END COMPONENT;
    
   constant MOORE       : integer   := 10;   
   constant MEALY       : integer   := 20;
   
   -----------------------------------------------------
   -----------------------------------------------------
   -- Zvolte parametry detektoru posloupnosti         --
   -- (nevyhovující typ automatu smažte/zakomentujte) --
   -----------------------------------------------------
   -----------------------------------------------------
   constant FSM_TYPE  :  integer  := MOORE;    -- typ realizace, zde pomocí automatu typu MOORE
   --constant FSM_TYPE  :  integer  := MEALY;  -- typ realizace, zde pomocí automatu typu MEALY
   -----------------------------------------------------
   -----------------------------------------------------
   constant SEQUENCE_1      :  std_logic_vector := "0111";
   constant SEQUENCE_2      :  std_logic_vector := "0010"; 
   constant TEST_SEQUENCE_1 :  std_logic_vector := "0111001011100111";
   constant CONTROL_VECTOR_1:  std_logic_vector := "00001000100000001";
   constant TEST_SEQUENCE_2 :  std_logic_vector := "1000101010000111111";
   constant CONTROL_VECTOR_2:  std_logic_vector := "0000001000000000100";
   constant TEST_SEQUENCE_3 :  std_logic_vector := "000100011000111111";  
   constant CONTROL_VECTOR_3:  std_logic_vector := "0000010000000001000";
   -----------------------------------------------------
   -----------------------------------------------------
   ----------------------------------------------------- 

   -- vstupní signály ovládané testovací sadou
   signal CLK : std_logic := '0';
   signal BTN : std_logic := '0';
   signal RESET : std_logic := '0';
   signal CE  : std_logic := '0';
   signal SWITCH : std_logic := '0';

   -- výstupní signály ovládané testovací sadou
   signal DETECT    : std_logic;
   signal S0       : std_logic;
   signal S1       : std_logic;
   signal S2       : std_logic;

   -- definice periody hodin
   constant CLK_period : time := 10 ns;
   constant BTN_period : time := 30 * CLK_period;
 
BEGIN
 
    -- mapování vstupù a výstupù testované komponenty na signály ovládané testovací sadou
   uut: seq_detector_wrapper PORT MAP (
          CLK       => CLK,
          BTN       => BTN,
          RESET     => RESET,
          CE        => CE,
          SWITCH    => SWITCH,
          DETECT    => DETECT,
          S0       =>  S0,
          S1       =>  S1,
          S2       =>  S2
        );

   -- paralelní proces obsluhující funkci hodin
   CLK_process :process
   begin
        CLK <= '1';
        wait for CLK_period/2;
        CLK <= '0';
        wait for CLK_period/2;
   end process;
 

   BTN_process :process
   begin
        BTN <= '1';
        wait for BTN_period/2;
        BTN <= '0';
        wait for BTN_period/2;
   end process;
 

   -- simulaèní proces realizující chování testovací sady
   stim_proc: process
      variable i  :  integer;
   begin        
      -- poèáteèní nastavení
      CE       <= '1';
      RESET    <= '1';
      SWITCH   <= '0';
      wait for 100 ns;  

      ---------------------------------------
      ---------------------------------------
      -- testovaní první posloupnosti      --
      -- vektor SEQUENCE_1                 --
      ---------------------------------------
      ---------------------------------------
      report "Testovani 1. posloupnosti:"   
      severity NOTE;
      
      for i in 0 to SEQUENCE_1'LENGTH-1 loop
         wait until BTN = '1';
         wait for 1 ns;
         RESET    <= '0';
         SWITCH   <= SEQUENCE_1(i);
      end loop;
      
      if (FSM_TYPE=moore) then
         wait until BTN = '1';
      end if;
      
      wait for CLK_period/2;
      assert DETECT='1'
      report "detekce prvni posloupnosti neprosla"
      severity ERROR;
      wait for CLK_period/2;

      
      wait for BTN_period*10;
      RESET    <= '1';
      wait until BTN = '1';
      ---------------------------------------
      ---------------------------------------
      
      ---------------------------------------
      ---------------------------------------
      -- testovaní druhé posloupnosti      --
      -- vektor SEQUENCE_2                 --
      ---------------------------------------
      ---------------------------------------
      report "Testovani 2. posloupnosti:"   
      severity NOTE;
      
      for i in 0 to SEQUENCE_2'LENGTH-1 loop
         wait until BTN = '1';
         wait for 1 ns;
         RESET    <= '0';
         SWITCH   <= SEQUENCE_2(i);
      end loop;
      
      if (FSM_TYPE=moore) then
         wait until BTN = '1';
      end if;
      
      wait for CLK_period/2;
      assert DETECT='1'
      report "detekce druhe posloupnosti neprosla"
      severity ERROR;
      wait for CLK_period/2;
      
      wait for BTN_period*10;
      RESET    <= '1';
      wait until BTN = '1';
      ---------------------------------------
      ---------------------------------------
      
      ---------------------------------------
      ---------------------------------------
      -- testovaní pomocí definovaného     --
      -- testovacího a kontrolního vektoru --
      -- vektor TEST_SEQUENCE_1            --
      ---------------------------------------
      ---------------------------------------
      report "Testovani 1. testovaciho vektoru:"    
      severity NOTE;
      
      for i in 0 to TEST_SEQUENCE_1'LENGTH-1 loop
         wait until BTN = '1';
         wait for 1 ns;
         RESET    <= '0';
         SWITCH   <= TEST_SEQUENCE_1(i);
         
         if (FSM_TYPE=moore) then
            assert DETECT=CONTROL_VECTOR_1(i)
            report "vystup neodpovida pozadovane hodnote"
            severity ERROR;
         elsif (FSM_TYPE=mealy) then
            assert DETECT=CONTROL_VECTOR_1(i+1)
            report "vystup neodpovida pozadovane hodnote"
            severity ERROR;
         end if;
      
      end loop;
      
      wait for BTN_period*10;
      RESET    <= '1';
      wait for BTN_period;
      ---------------------------------------
      ---------------------------------------
      
      ---------------------------------------
      ---------------------------------------
      -- testovaní pomocí definovaného     --
      -- testovacího a kontrolního vektoru --
      -- vektor TEST_SEQUENCE_2            --
      ---------------------------------------
      ---------------------------------------
      report "Testovani 2. testovaciho vektoru:"    
      severity NOTE;
      
      for i in 0 to TEST_SEQUENCE_2'LENGTH-1 loop
         wait until BTN = '1';
         wait for 1 ns;
         RESET    <= '0';
         SWITCH   <= TEST_SEQUENCE_2(i);
         
         if (FSM_TYPE=moore) then
            assert DETECT=CONTROL_VECTOR_2(i)
            report "vystup neodpovida pozadovane hodnote"
            severity ERROR;
         elsif (FSM_TYPE=mealy) then
            assert DETECT=CONTROL_VECTOR_2(i+1)
            report "vystup neodpovida pozadovane hodnote"
            severity ERROR;
         end if;
      
      end loop;
        
      wait for BTN_period*10;
      RESET    <= '1';
      wait for BTN_period;
      ---------------------------------------
      ---------------------------------------
      
      ---------------------------------------
      ---------------------------------------
      -- testovaní pomocí definovaného     --
      -- testovacího a kontrolního vektoru --
      -- vektor TEST_SEQUENCE_3            --
      ---------------------------------------
      ---------------------------------------
      report "Testovani 3. testovaciho vektoru:"    
      severity NOTE;
      
      for i in 0 to TEST_SEQUENCE_3'LENGTH-1 loop
         wait until BTN = '1';
         wait for 1 ns;
         RESET    <= '0';
         SWITCH   <= TEST_SEQUENCE_3(i);
         
         if (FSM_TYPE=moore) then
            assert DETECT=CONTROL_VECTOR_3(i)
            report "vystup neodpovida pozadovane hodnote"
            severity ERROR;
         elsif (FSM_TYPE=mealy) then
            assert DETECT=CONTROL_VECTOR_3(i+1)
            report "vystup neodpovida pozadovane hodnote"
            severity ERROR;
         end if;
         
      end loop;
      
      wait for BTN_period*10;
      RESET    <= '1';
      wait for BTN_period;
      ---------------------------------------
      ---------------------------------------
      
      
      wait for 100 ns;
      
      -- konec testu lze vyvoval pouze selháním
      report "Test dokoncen" severity FAILURE;

      wait;
   end process;

END;
