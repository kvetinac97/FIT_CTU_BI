onbreak {quit -force}
onerror {quit -force}

asim -t 1ps +access +r +m+adder -L xil_defaultlib -L secureip -O5 xil_defaultlib.adder

do {wave.do}

view wave
view structure

do {adder.udo}

run -all

endsim

quit -force
