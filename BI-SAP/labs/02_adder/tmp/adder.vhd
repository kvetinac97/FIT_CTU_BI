library IEEE;
use IEEE.std_logic_1164.all;

library xil_defaultlib;
use xil_defaultlib.all;

entity adder_vhdl is
   port (
      A0   :  in  std_logic;
      A1   :  in  std_logic;
      A2   :  in  std_logic;
      A3   :  in  std_logic;
      B0   :  in  std_logic;
      B1   :  in  std_logic;
      B2   :  in  std_logic;
      B3   :  in  std_logic;
      Cin  :  in  std_logic;
      Cout :  out std_logic;
      S0   :  out std_logic;
      S1   :  out std_logic;
      S2   :  out std_logic;
      S3   :  out std_logic
   );
end entity adder_vhdl;

architecture Behavioral of adder_vhdl is
	component adder1bit
		port (
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
	end component;

	component adder1bit1
		port (
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
	end component;

	component adder1bit2
		port (
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
	end component;

	component adder1bit3
		port (
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
	end component;



      signal A_0_1           :    std_logic;   
      signal A_1_1           :    std_logic;   
      signal A_2_1           :    std_logic;   
      signal adder1bit1_Cout :    std_logic;   
      signal adder1bit1_S    :    std_logic;   
      signal adder1bit2_Cout :    std_logic;   
      signal adder1bit2_S    :    std_logic;   
      signal adder1bit3_Cout :    std_logic;   
      signal adder1bit3_S    :    std_logic;   
      signal B_0_1           :    std_logic;   
      signal B_1_1           :    std_logic;   
      signal B_2_1           :    std_logic;   
      signal I0_0_1          :    std_logic;   
      signal I1_0_1          :    std_logic;   
      signal I1_0_2          :    std_logic;   
      signal or_0_O          :    std_logic;   
      signal xor_1_O         :    std_logic;   
begin
	I0_0_1 <= A0;
	A_2_1 <= A1;
	A_1_1 <= A2;
	A_0_1 <= A3;
	I1_0_1 <= B0;
	B_0_1 <= B1;
	B_2_1 <= B2;
	B_1_1 <= B3;
	I1_0_2 <= Cin;
	Cout <= adder1bit3_Cout;
	S0 <= xor_1_O;
	S1 <= adder1bit1_S;
	S2 <= adder1bit2_S;
	S3 <= adder1bit3_S;


	-- adder1bit
	adder1bit_C : adder1bit
		port map (
			A    =>  I0_0_1,
			B    =>  I1_0_1,
			Cin  =>  I1_0_2,
			Cout =>  or_0_O,
			S    =>  xor_1_O
		);

	-- adder1bit1
	adder1bit1_C : adder1bit1
		port map (
			A    =>  A_2_1,
			B    =>  B_0_1,
			Cin  =>  or_0_O,
			Cout =>  adder1bit1_Cout,
			S    =>  adder1bit1_S
		);

	-- adder1bit2
	adder1bit2_C : adder1bit2
		port map (
			A    =>  A_1_1,
			B    =>  B_2_1,
			Cin  =>  adder1bit1_Cout,
			Cout =>  adder1bit2_Cout,
			S    =>  adder1bit2_S
		);

	-- adder1bit3
	adder1bit3_C : adder1bit3
		port map (
			A    =>  A_0_1,
			B    =>  B_1_1,
			Cin  =>  adder1bit2_Cout,
			Cout =>  adder1bit3_Cout,
			S    =>  adder1bit3_S
		);

end Behavioral;

library IEEE;
use IEEE.std_logic_1164.all;

entity adder1bit is
	port(
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
end entity adder1bit;

architecture Behavioral of adder1bit is

	signal and_0_O :    std_logic;
	signal and_1_O :    std_logic;
	signal I0_0_1  :    std_logic;
	signal I1_0_1  :    std_logic;
	signal I1_0_2  :    std_logic;
	signal or_0_O  :    std_logic;
	signal xor_0_O :    std_logic;
	signal xor_1_O :    std_logic;
begin
	I0_0_1 <= A;
	I1_0_1 <= B;
	I1_0_2 <= Cin;
	Cout <= or_0_O;
	S <= xor_1_O;

	-- and_0
	and_0_O <= I0_0_1 and I1_0_1;

	-- and_1
	and_1_O <= xor_0_O and I1_0_2;

	-- or_0
	or_0_O <= and_0_O or and_1_O;

	-- xor_0
	xor_0_O <= I0_0_1 xor I1_0_1;

	-- xor_1
	xor_1_O <= xor_0_O xor I1_0_2;

end Behavioral;

library IEEE;
use IEEE.std_logic_1164.all;

entity adder1bit1 is
	port(
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
end entity adder1bit1;

architecture Behavioral of adder1bit1 is

	signal and_0_O :    std_logic;
	signal and_1_O :    std_logic;
	signal I0_0_1  :    std_logic;
	signal I1_0_1  :    std_logic;
	signal I1_0_2  :    std_logic;
	signal or_0_O  :    std_logic;
	signal xor_0_O :    std_logic;
	signal xor_1_O :    std_logic;
begin
	I0_0_1 <= A;
	I1_0_1 <= B;
	I1_0_2 <= Cin;
	Cout <= or_0_O;
	S <= xor_1_O;

	-- and_0
	and_0_O <= I0_0_1 and I1_0_1;

	-- and_1
	and_1_O <= xor_0_O and I1_0_2;

	-- or_0
	or_0_O <= and_0_O or and_1_O;

	-- xor_0
	xor_0_O <= I0_0_1 xor I1_0_1;

	-- xor_1
	xor_1_O <= xor_0_O xor I1_0_2;

end Behavioral;

library IEEE;
use IEEE.std_logic_1164.all;

entity adder1bit2 is
	port(
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
end entity adder1bit2;

architecture Behavioral of adder1bit2 is

	signal and_0_O :    std_logic;
	signal and_1_O :    std_logic;
	signal I0_0_1  :    std_logic;
	signal I1_0_1  :    std_logic;
	signal I1_0_2  :    std_logic;
	signal or_0_O  :    std_logic;
	signal xor_0_O :    std_logic;
	signal xor_1_O :    std_logic;
begin
	I0_0_1 <= A;
	I1_0_1 <= B;
	I1_0_2 <= Cin;
	Cout <= or_0_O;
	S <= xor_1_O;

	-- and_0
	and_0_O <= I0_0_1 and I1_0_1;

	-- and_1
	and_1_O <= xor_0_O and I1_0_2;

	-- or_0
	or_0_O <= and_0_O or and_1_O;

	-- xor_0
	xor_0_O <= I0_0_1 xor I1_0_1;

	-- xor_1
	xor_1_O <= xor_0_O xor I1_0_2;

end Behavioral;

library IEEE;
use IEEE.std_logic_1164.all;

entity adder1bit3 is
	port(
			A    :  in  std_logic;
			B    :  in  std_logic;
			Cin  :  in  std_logic;
			Cout :  out std_logic;
			S    :  out std_logic
		);
end entity adder1bit3;

architecture Behavioral of adder1bit3 is

	signal and_0_O :    std_logic;
	signal and_1_O :    std_logic;
	signal I0_0_1  :    std_logic;
	signal I1_0_1  :    std_logic;
	signal I1_0_2  :    std_logic;
	signal or_0_O  :    std_logic;
	signal xor_0_O :    std_logic;
	signal xor_1_O :    std_logic;
begin
	I0_0_1 <= A;
	I1_0_1 <= B;
	I1_0_2 <= Cin;
	Cout <= or_0_O;
	S <= xor_1_O;

	-- and_0
	and_0_O <= I0_0_1 and I1_0_1;

	-- and_1
	and_1_O <= xor_0_O and I1_0_2;

	-- or_0
	or_0_O <= and_0_O or and_1_O;

	-- xor_0
	xor_0_O <= I0_0_1 xor I1_0_1;

	-- xor_1
	xor_1_O <= xor_0_O xor I1_0_2;

end Behavioral;
