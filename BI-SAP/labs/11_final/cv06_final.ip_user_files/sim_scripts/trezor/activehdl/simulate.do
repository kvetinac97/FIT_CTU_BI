onbreak {quit -force}
onerror {quit -force}

asim -t 1ps +access +r +m+trezor -L xil_defaultlib -L secureip -O5 xil_defaultlib.trezor

do {wave.do}

view wave
view structure

do {trezor.udo}

run -all

endsim

quit -force
