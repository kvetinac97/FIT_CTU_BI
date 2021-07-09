----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 27.03.2019 04:07:28
-- Design Name: 
-- Module Name: trezorKoder - Behavioral
-- Project Name: 
-- Target Devices: 
-- Tool Versions: 
-- Description: 
-- 
-- Dependencies: 
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
-- 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity trezorKoder is
    Port ( up : in STD_LOGIC;
           right : in STD_LOGIC;
           down : in STD_LOGIC;
           left : in STD_LOGIC;
           buttons1 : out STD_LOGIC;
           buttons0 : out STD_LOGIC);
end trezorKoder;

architecture Behavioral of trezorKoder is

begin
koderPrc : process (up, right, down, left)
begin
    buttons0 <= '0';
    buttons1 <= '0';
    if up = '1' then
        buttons1 <= '0';
        buttons0 <= '0';
    end if;    
    if right = '1' then
        buttons1 <= '0';
        buttons0 <= '1';
    end if;
    if down = '1' then
        buttons1 <= '1';
        buttons0 <= '0';
    end if;
    if left = '1' then
        buttons1 <= '1';
        buttons0 <= '1';
    end if;
end process;

end Behavioral;
