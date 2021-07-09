library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use STD.textio.all;                     -- basic I/O
use IEEE.std_logic_textio.all;
use IEEE.Numeric_Std.all;
use ieee.std_logic_unsigned.all;

entity l5_posloupnost_test_v2_wrapper is
    generic(
        C_FSM_TYPE         : string                       := "MOORE"; -- FSM type - MEALY or MOORE
        C_SEQUENCE_1       : std_logic_vector(3 downto 0) := "0111";
        C_SEQUENCE_2       : std_logic_vector(3 downto 0) := "0010";
        C_MAX_EXTRA_LENGTH : integer                      := 3 -- test difficulty (default is 3 - extra "dirty" bits before correct sequence)
    );
end l5_posloupnost_test_v2_wrapper;

architecture Behavioral of l5_posloupnost_test_v2_wrapper is

    component seq_detector_wrapper
       Port(
        clk    : in  std_logic;
        btn    : in  std_logic;
        reset  : in  std_logic;
        switch : in  std_logic;
        detect : out std_logic;
        s0     : out std_logic;
        s1     : out std_logic;
        s2     : out std_logic
    );
    end component;

    signal Reset : std_logic := '0';
    signal Clk : std_logic := '1';
    signal btn : std_logic := '1';
    signal debounce_out_main_clk: std_logic := '0';
    signal ce : std_logic := '1';
    signal q : std_logic_vector(2 downto 0) := "000";
    signal sw : std_logic := '0';
    signal y, y_reference : std_logic := '0';
    signal reference_shift_reg: std_logic_vector(3 downto 0) := "0000";
    signal reference_delay: integer := 0;
    signal terminate : boolean := false;
    constant CLK_PERIOD : time := 4 ns;
    constant BTN_period : time := 20 * CLK_period;
    
    signal debounce_reset: std_logic := '0';
    signal debounce_ce: std_logic := '0';
    signal debounce_cnt: std_logic_vector(3 downto 0) := (others => '0');
    signal debounce_btn_prev: std_logic := '0';

    
    impure function print_error return line is
        variable file_line     : line;
    begin
        write(file_line, string'("  Time = "));
        write(file_line, NOW);
        write(file_line, string'("  State = 0x"));
        hwrite(file_line, q);
        write(file_line, string'("  SW = "));
        write(file_line, sw);

        write(file_line, string'(" Output = 0x"));
        write(file_line, y);
        write(file_line, string'(" Correct = 0x"));
        write(file_line, y_reference);
        write(file_line, string'(" Last (max. 4) received inputs after reset (oldest is left) = "));
        write(file_line, reference_shift_reg(reference_delay-1 downto 0));
        write(file_line, string'(" - ERROR"));
        return file_line;
    end;
    
    procedure make_reset (signal reset, sw: out std_logic) is
    begin
        sw <= '0';
        Reset <= '1';
        wait until rising_edge(debounce_out_main_clk);
        wait for BTN_period/10;
        Reset <= '0';
    end make_reset;

    procedure make_test (sequence_length: integer; tested_sequence: std_logic_vector; signal sw: out std_logic; variable out_error: out boolean) is
    variable file_line : line;
    variable var_error : boolean;
    begin
        write(file_line, string'("Testing sequence "));
        write(file_line, tested_sequence(sequence_length-1 downto 0));
--        writeline(output, file_line); -- write to display
        var_error := false;
        for i in 0 to sequence_length loop
            if i < sequence_length then
                sw <= tested_sequence(sequence_length-1 - i);
            else
                sw <= '0';
            end if;
    
            wait until falling_edge(debounce_out_main_clk);
    
            if (y /= y_reference) then
                if (not var_error) then 
                    writeline(output, file_line); -- zapsat uvodni hlasku pred prvni chybou
                end if;
                file_line := print_error;
                writeline(output, file_line);
                var_error := true;
            end if;
            wait until rising_edge(debounce_out_main_clk);
            wait for BTN_period/10;
        end loop;
    
        if (not var_error) then
            write(file_line, string'(" ... OK"));
            writeline(output, file_line); -- write to display
        end if;
        
        out_error := var_error;
    end make_test;

begin
    
    uut: seq_detector_wrapper 
    PORT MAP (
       clk     => clk,
       btn     => btn,
       reset   => reset,
       switch  => sw,
       detect  => y,
       s0      =>  q(0),
       s1      =>  q(1),
       s2      =>  q(2)
    );

    buzzer: process
    begin
        wait for 1000 ns;
        if (not terminate) then
            report ("Simulation paused - press Run-all to continue!");
        end if;
        wait;
    end process;
    
    process
    begin
        clk <= not clk;
        wait for CLK_PERIOD/2;
        if (terminate) then
            wait;
        end if;
    end process;

   btn_process :process
   begin
        btn <= not btn;
        wait for BTN_period/2;
        if (terminate) then
            wait;
        end if;
   end process;
    
    reference_shift_proc: process (debounce_out_main_clk, Reset)
    begin
        if (Reset = '1') then
            reference_shift_reg <= "0000";
        elsif (debounce_out_main_clk'event and debounce_out_main_clk = '1') then
            reference_shift_reg <= reference_shift_reg(2 downto 0) & sw;
        end if;
    end process;
    
    reference_delay_proc: process (debounce_out_main_clk, Reset)
    begin
        if (Reset = '1') then
            reference_delay <= 0;
        elsif (debounce_out_main_clk'event and debounce_out_main_clk = '1') then
            if (y_reference = '1') then
                reference_delay <= 0;
            elsif (reference_delay < 4) then
                reference_delay <= reference_delay + 1;
            end if;
        end if;
    end process;
    
    reference_out_proc: process (reference_shift_reg, sw)
    begin
        if (C_FSM_TYPE = "MOORE") then
            if (((reference_shift_reg = C_SEQUENCE_1) or (reference_shift_reg = C_SEQUENCE_2))
                    and (reference_delay >= 4)) then
               y_reference <= '1';
            else 
               y_reference <= '0';
            end if;
        else -- Mealy
            if (((reference_shift_reg(2 downto 0) & sw = C_SEQUENCE_1) or (reference_shift_reg(2 downto 0) & sw = C_SEQUENCE_2))
                  and (reference_delay >= 3)) then
               y_reference <= '1';
            else 
               y_reference <= '0';
            end if;
        end if;   
    end process;

    stim_proc: process
    variable file_line      : line;
    variable var_error      : boolean; 
    variable var_no_errors  : integer; 
    
    variable tested_sequence: std_logic_vector(3+C_MAX_EXTRA_LENGTH downto 0); -- 4 bity + extra
    
    begin
        terminate <= false;
        var_no_errors := 0;
        ce <= '1';

        make_reset (reset, sw);
        
        write(file_line, string'("### Simulation start ###"));
        writeline(output, file_line); -- write to display
        
        for i in 0 to C_MAX_EXTRA_LENGTH loop
            for j in 0 to 2**i-1 loop
                tested_sequence := std_logic_vector(to_unsigned(j, C_MAX_EXTRA_LENGTH)) & C_SEQUENCE_1;
                make_test (i+4, tested_sequence, sw, var_error);
                
                if (var_error) then
                    var_no_errors := var_no_errors+1;
                end if;

                make_reset (reset, sw);

                tested_sequence := std_logic_vector(to_unsigned(j, C_MAX_EXTRA_LENGTH)) & C_SEQUENCE_2;
                make_test (i+4, tested_sequence, sw, var_error);

                if (var_error) then
                    var_no_errors := var_no_errors+1;
                end if;

                make_reset (reset, sw);
            end loop;
            
        end loop;

       write(file_line, string'("### Total number of errors = "));
       write(file_line, var_no_errors);
       writeline(output, file_line);              -- write to display    

       write(file_line, string'("### Simulation finished ###"));
       writeline(output, file_line);              -- write to display    
       
       terminate <= true;
       
       wait;
    end process;
    
    
    
    -- debounce copy
    counter: process(clk)
    begin
        if rising_edge(clk) then 
            if (debounce_reset = '1') then
                debounce_cnt <= (others => '0');
            elsif (ce = '1') then
                debounce_cnt <= debounce_cnt + 1;
            end if;
        end if;
    end process;

    process(clk)
    begin
        if rising_edge(clk) then
            if (debounce_btn_prev /= btn) then
                debounce_reset <= '1';
            else 
                debounce_reset <= '0';
            end if;
        end if;
    end process;
                
    process(clk)
    begin
        if rising_edge(clk) then
            if (debounce_reset = '1') then
                debounce_btn_prev <= btn;
            end if;
        end if;
    end process;
                
    process(clk)
    begin
        if rising_edge(clk) then
            if ((debounce_cnt(3) = '1') and (debounce_cnt(0) = '0')) then
                debounce_out_main_clk <= debounce_btn_prev;
            end if;
        end if;
    end process;
                

    debounce_ce <= '0' when ((debounce_cnt(3) = '1') and (debounce_cnt(0) = '1'))
       else '1';
end Behavioral;
