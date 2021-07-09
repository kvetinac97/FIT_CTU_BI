-- knihovna typů pro práci
library STD;
use STD.textio.all;                     -- basic I/O

library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_ARITH.all;
use IEEE.STD_LOGIC_UNSIGNED.all;
use IEEE.std_logic_textio.all;
  
-- prázdná entita zapouzdřující testovací sadu
entity automat_test is
	generic(
		C_MODULO	: natural	:= 6;		    -- counter mod <C_MODULO> // citac modulo <C_MODULO>
		C_FSM_TYPE	: string	:= "MOORE";	    -- counter type: MEALY or MOORE // typ citace: MEALY nebo MOORE
        C_OUT_TYPE	: string	:= "GRAY";	    -- counter output type: BINARY or GRAY // typ vystupu citace: BINARY nebo GRAY
		C_DIR       : boolean   := False        -- True pokud má automat vstup určující směr, jinak False
	);
end automat_test;
 
-- popis chování testovací sady
architecture behavior OF automat_test is

  procedure print_check(
    inc : std_logic;
    
    d : std_logic_vector(2 downto 0);
    y : std_logic_vector(2 downto 0);
    c : std_logic_vector(2 downto 0)
  ) is
    variable file_line     : line;
  begin
	write(file_line, string'("  INC = "));
	write(file_line, inc);
	
	write(file_line, string'(" Next = 0x"));
	hwrite(file_line, d);
	write(file_line, string'(" Output = 0x"));
	hwrite(file_line, y);
	write(file_line, string'(" Correct = 0x"));
    hwrite(file_line, c);
    if y /= c then
	   write(file_line, string'(" - ERROR"));
	else
       write(file_line, string'(" - OK"));
    end if;
    writeline(output, file_line);              -- write to display
  end;
 
   -- definice vstupů a výstupů testovaných komponent
	component output_logic
		port(
			q0    : in  std_logic;
    		q1    : in  std_logic;
    		q2    : in  std_logic;
    		INC   : in  std_logic;
    		DIR   : in  std_logic;
    		y0    : out std_logic;
    		y1    : out std_logic;
    		y2    : out std_logic
		);
	end component;
    
	component next_state_logic
		Port (
			q0    : in  std_logic;
			q1    : in  std_logic;
			q2    : in  std_logic;
			INC   : in  std_logic;
			DIR   : in  std_logic;
			d0    : out std_logic;
			d1    : out std_logic;
			d2    : out std_logic
		);
	end component;

	-- vnitřní signály ovládané testovací sadou s počátečním nastavením
	signal d	: std_logic_vector(2 downto 0) := "000";
	signal q	: std_logic_vector(2 downto 0) := "000";
	signal dir	: std_logic := '1';
	signal inc	: std_logic := '0';

	signal Reset	: std_logic := '0';
	signal Clk		: std_logic := '1';

	-- výstupní signály automatu
	signal y : std_logic_vector(2 downto 0);

	constant CLK_PERIOD : time := 10 ns;

begin
 
	-- mapování vstupů a výstupů next_state_logic komponenty
	next_state_logic_uut: next_state_logic
		port map(
			q0    => q(0),
			q1    => q(1),
			q2    => q(2),
			INC   => inc,
			DIR   => dir,
			d0    => d(0),
			d1    => d(1),
			d2    => d(2)
		);
	
	-- mapování vstupů a výstupů output_logic komponenty
	output_logic_uut: output_logic
		port map(
			q0    => q(0),
    		q1    => q(1),
    		q2    => q(2),
    		INC   => inc,
    		DIR   => dir,
    		y0    => y(0),
    		y1    => y(1),
    		y2    => y(2)
		);

	-- generování hodinových pulsů
	Clk <= not Clk after CLK_PERIOD / 2;
	
	-- klopné obvody
	process (Clk, Reset)
	begin
		if Reset = '1' then
			q	<= (others => '0');
		elsif (Clk'event and Clk = '1') then
			q	<= d;
		end if;
	end process;
 
   -- simulační proces realizující chování testovací sady
	stim_proc: process
		type T_DATA is ARRAY (0 to 7) of std_logic_vector(2 downto 0);
		constant binary: T_DATA := (
			"000", "001", "010", "011", "100", "101", "110", "111"
		);
		constant gray: T_DATA := (
			"000", "001", "011", "010", "110", "111", "101", "100"
		);
		variable j            : natural range 0 to 7 := 0;
		variable file_line    : line;
	begin
	   -- zacatekc simulace
        write(file_line, string'("### Simulation start ###"));
        writeline(output, file_line);              -- write to display	

		Reset <= '1';
		wait for 3*CLK_PERIOD;
      	Reset <= '0';
      	if C_DIR then
      	   write(file_line, string'("DIR = 1"));
	       writeline(output, file_line);
	    end if;

		for i in 0 to C_MODULO-1 loop
		    wait for CLK_PERIOD/2;
			j := i;
			
			write(file_line, string'(" State = 0x"));
	        hwrite(file_line, q);
			writeline(output, file_line);
						    
			if C_OUT_TYPE = "GRAY" then
				print_check(inc, d, y, gray(j));
			else
			    print_check(inc, d, y, binary(j));    
			end if;
			
			wait until rising_edge(Clk);
			
			inc <= '1';
			if C_FSM_TYPE = "MEALY" then
				j := 0;
				if i < C_MODULO-1 then
					j := i+1;
				end if;
			end if;
			
			wait for CLK_PERIOD/2;
			
			if C_OUT_TYPE = "GRAY" then
				print_check(inc, d, y, gray(j));
			else
			    print_check(inc, d, y, binary(j));    
			end if;
			
			wait until rising_edge(Clk);
			inc <= '0';
		end loop;
      
      -- druhá část pro dir = 0 -> dekrementace
      if C_DIR then
        dir <= '0';
        write(file_line, string'("DIR = 0"));
	    writeline(output, file_line);
      
	   wait for CLK_PERIOD/2;
	   j := 0;
			
		write(file_line, string'(" State = 0x"));
	    hwrite(file_line, q);
		writeline(output, file_line);
						    
		if C_OUT_TYPE = "GRAY" then
			print_check(inc, d, y, gray(j));
		else
		    print_check(inc, d, y, binary(j));    
		end if;
			
		wait until rising_edge(Clk);

		inc <= '1';
		if C_FSM_TYPE = "MEALY" then
    		j := C_MODULO-1;
		end if;
			
		wait for CLK_PERIOD/2;
		
		if C_OUT_TYPE = "GRAY" then
			print_check(inc, d, y, gray(j));
		else
		    print_check(inc, d, y, binary(j));    
		end if;
		
		wait until rising_edge(Clk);
		inc <= '0';
      
        for i in C_MODULO-1 downto 0 loop
		    wait for CLK_PERIOD/2;
			j := i;
			
			write(file_line, string'(" State = 0x"));
	        hwrite(file_line, q);
			writeline(output, file_line);
						    
			if C_OUT_TYPE = "GRAY" then
				print_check(inc, d, y, gray(j));
			else
			    print_check(inc, d, y, binary(j));    
			end if;
			
			wait until rising_edge(Clk);
			inc <= '1';
			
			if C_FSM_TYPE = "MEALY" then
				j := C_MODULO-1;
				if i > 0 then
					j := i-1;
				end if;
			end if;
			
			wait for CLK_PERIOD/2;
			
			if C_OUT_TYPE = "GRAY" then
				print_check(inc, d, y, gray(j));
			else
			    print_check(inc, d, y, binary(j));    
			end if;
			
			wait until rising_edge(Clk);
			inc <= '0';
		  end loop;
	   end if;
      
      -- konec simulace
       write(file_line, string'("### Simulation end ###"));
       writeline(output, file_line);              -- write to display									
	   
	   wait;	
	end process;

end;
