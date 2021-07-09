library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use STD.textio.all;                     -- basic I/O

use IEEE.std_logic_textio.all;
use IEEE.Numeric_Std.all;

entity l5_posloupnost_test_v2 is
    generic(
        C_FSM_TYPE         : string                       := "MOORE"; -- FSM type - MEALY or MOORE
        C_SEQUENCE_1       : std_logic_vector(3 downto 0) := "0010";
        C_SEQUENCE_2       : std_logic_vector(3 downto 0) := "0111";
        C_MAX_EXTRA_LENGTH : integer                      := 3 -- test difficulty (default is 3 - extra "dirty" bits before correct sequence)
    );
end l5_posloupnost_test_v2;

architecture Behavioral of l5_posloupnost_test_v2 is

    component output_logic
--    Generic (
--        C_SEQUENCE_1 : std_logic_vector(3 downto 0) := C_SEQUENCE_1;
--        C_SEQUENCE_2 : std_logic_vector(3 downto 0) := C_SEQUENCE_2;
--        C_FSM_TYPE: string := "MOORE"
--    );
        port(
            q0    : in  std_logic;
            q1    : in  std_logic;
            q2    : in  std_logic;
            sw    : in  std_logic;
            y     : out std_logic
        );
    end component;
    
    component next_state_logic
--    Generic (
--        C_SEQUENCE_1 : std_logic_vector(3 downto 0) := C_SEQUENCE_1;
--        C_SEQUENCE_2 : std_logic_vector(3 downto 0) := C_SEQUENCE_2;
--        C_FSM_TYPE: string := "MOORE"
--    );
        Port (
            q0    : in  std_logic;
            q1    : in  std_logic;
            q2    : in  std_logic;
            sw    : in  std_logic;
            d0    : out std_logic;
            d1    : out std_logic;
            d2    : out std_logic
        );
    end component;

    signal Reset : std_logic := '0';
    signal Clk : std_logic := '1';
    signal d : std_logic_vector(2 downto 0) := "000";
    signal q : std_logic_vector(2 downto 0) := "000";
    signal sw : std_logic := '0';
    signal y, y_reference : std_logic := '0';
    signal reference_shift_reg: std_logic_vector(3 downto 0) := "0000";
    signal reference_delay: integer := 0;
    signal terminate : boolean := false;
    constant CLK_PERIOD : time := 4 ns;

    
    impure function print_error return line is
        variable file_line     : line;
    begin
        write(file_line, string'("  Time = "));
        write(file_line, NOW);
        write(file_line, string'("  State = 0x"));
        hwrite(file_line, q);
        write(file_line, string'("  SW = "));
        write(file_line, sw);

        write(file_line, string'(" Next = 0x"));
        hwrite(file_line, d);
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
        wait until rising_edge(clk);
        wait for 1 ns;
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
    
            wait until falling_edge(Clk);
    
            if (y /= y_reference) then
                if (not var_error) then 
                    writeline(output, file_line); -- zapsat uvodni hlasku pred prvni chybou
                end if;
                file_line := print_error;
                writeline(output, file_line);
                var_error := true;
            end if;
            wait until rising_edge(Clk);
            wait for 1 ns;
        end loop;
    
        if (not var_error) then
            write(file_line, string'(" ... OK"));
            writeline(output, file_line); -- write to display
        end if;
        
        out_error := var_error;
    end make_test;

begin
    
    next_state_logic_uut: next_state_logic
--    generic map(
--        C_SEQUENCE_1 => C_SEQUENCE_1,
--        C_SEQUENCE_2 => C_SEQUENCE_2,
--        C_FSM_TYPE => C_FSM_TYPE
--    )
    port map(
        q0    => q(0),
        q1    => q(1),
        q2    => q(2),
        sw    => sw,
        d0    => d(0),
        d1    => d(1),
        d2    => d(2)
    );


    output_logic_uut: output_logic
--    generic map(
--        C_SEQUENCE_1 => C_SEQUENCE_1,
--        C_SEQUENCE_2 => C_SEQUENCE_2,
--        C_FSM_TYPE => C_FSM_TYPE
--    )
    port map(
        q0    => q(0),
        q1    => q(1),
        q2    => q(2),
        sw    => sw,
        y     => y
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

    process (Clk, Reset)
    begin
        if (Reset = '1') then
            q    <= (others => '0');
        elsif (Clk'event and Clk = '1') then
            q    <= d;
        end if;
    end process;
    
    reference_shift_proc: process (Clk, Reset)
    begin
        if (Reset = '1') then
            reference_shift_reg <= "0000";
        elsif (Clk'event and Clk = '1') then
            reference_shift_reg <= reference_shift_reg(2 downto 0) & sw;
        end if;
    end process;
    
    reference_delay_proc: process (Clk, Reset)
    begin
        if (Reset = '1') then
            reference_delay <= 0;
        elsif (Clk'event and Clk = '1') then
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
end Behavioral;
