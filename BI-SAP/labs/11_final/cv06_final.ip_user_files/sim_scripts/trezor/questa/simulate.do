onbreak {quit -f}
onerror {quit -f}

vsim -t 1ps -lib xil_defaultlib trezor_opt

do {wave.do}

view wave
view structure
view signals

do {trezor.udo}

run -all

quit -force
