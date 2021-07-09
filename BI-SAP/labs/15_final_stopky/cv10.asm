; Prepnuti do pameti dat
.dseg

; Rezervace mista
.org 0x100
flag: .byte 1

; Prepnuti do pameti programu
.cseg
.include "m169def.inc" ; nas procesor

; Prace s displejem
.org 0x1000
.include "print.inc"

; Zacatek programu (uplny / po preruseni)
.org 0
jmp	start
.org 0xA
jmp interrupt

.org 0x100

start:
	; Inicializace zasobniku
	ldi r16, 0xFF
	out SPL, r16
	ldi r16, 0x04
	out SPH, r16

    ; Inicializace displeje
	call init_disp

	; Inicializace joysticku
    call init_joy

	; Nastaveni stopek
	call reset_stopky

    call init_int ; Inicializace preruseni od casovace
	call main_loop
	jmp end

; Hlavni vykonova cast programu
main_loop:
	; nacteni a otestovani hodnoty flag-u
    lds r20, flag
    cpi r20, 0
    breq neni_flag

    ldi r20, 0       ; vycisteni flag-u
    sts flag, r20
    inc r16          ; reakce na preruseni

	; Precte stav joysticku do r1, pokud je stisknuty, spusti/zastavi stopky
	call read_joy
	mov r20, r1

	; reset stopek (r1=1, joystick enter)
	cpi r20, 1
	breq reset_stopky_

	; zastaveni stopek (r1=2, joystick vlevo)
	cpi r20, 2
    breq stop_stopky

	; spusteni stopek (r1=3, joystick vpravo)
	cpi r20, 3
	breq start_stopky

	; nic neni spusteno, pokud maji byt pustene, pustime stopky
	cpi r26, 1
	breq stopky

	jmp main_loop ; stopky nebezi - opakujeme

reset_stopky_:
	call reset_stopky
	jmp main_loop

reset_stopky:
	ldi r21, '0' ; minuty 1
	ldi r22, '0' ; minuty 2
	ldi r23, '0' ; sekundy 1
	ldi r24, '0' ; sekundy 2
	ldi r25, '0' ; šestáctiny sekundy
	ldi r26,  0  ; stav stopek (0 = zastaveny, 1 = bìží)
	call show_stopky
	ret

start_stopky:
	ldi r26, 1
	jmp stopky

stop_stopky:
	ldi r26,  0
	jmp main_loop

stopky:
	call inc_stopky
	call show_stopky

neni_flag:
    jmp main_loop

end: jmp end ; konec programu

init_int:
    cli              ; globalni zakazani preruseni
    ldi r16, 0b00001000
    sts ASSR, r16    ; vyber hodin od externiho krystaloveho oscilátoru 32768 Hz
    ldi r16, 0b00000001
    sts TIMSK2, r16  ; povoleni preruseni od casovace 2
    ldi r16, 0b00000010
    sts TCCR2A, r16  ; nastaveni deliciho pomeru 8
    clr r16
    out EIMSK, r16   ; zakazani preruseni od joysticku
    sei              ; globalni povoleni preruseni
ret

; Preruseni samotne
interrupt:
    push r16
    in r16, SREG
    push r16         ; uklid SREG

    ldi  r16, 1
    sts  flag, r16   ; nastaveni flag-u

    pop r16
    out SREG, r16
    pop r16          ; obnoveni SREG
reti                 ;

; Inkrementuje postupne jednotlive casti stopek

inc_stopky:
	ldi r16, 0x01
	add r25, r16

	cpi r25, 0x40 ; šestnáctiny >= 16
	breq inc_sec
ret

inc_sec:
	ldi r25, '0'

	ldi r16, 0x01
	add r24, r16

	cpi r24, 0x3A ; sekundy dolni >= 10
	breq inc_sec1
ret

inc_sec1:
	ldi r24, '0'

	ldi r16, 0x01
	add r23, r16

	cpi r23, '6' ; sekundy horni >= 6
	breq inc_min
ret

inc_min:
	ldi r23, '0'

	ldi r16, 0x01
	add r22, r16

	cpi r22, 0x3A ; minuty dolni >= 10
	breq inc_min1
ret

inc_min1:
	ldi r22, '0'

	ldi r16, 0x01
	add r21, r16

	cpi r21, '6' ; minuty horni >= 6
	breq stopprogram
ret

; Stopky uz nemaji kam dal bezet
stopprogram:
  ldi r21, '5'
  rjmp PC ; nekonecna smycka

; Ukaze stopky na displej
show_stopky:

	; minuty 1
	mov r16, r21
	ldi r17, 2
	call show_char

	; minuty 2
	mov r16, r22
	ldi r17, 3
	call show_char

	ldi r16, 0x01
	sts LCDDR8, r16 ;dvojtecka mezi casem

	; sekundy 1
	mov r16, r23
	ldi r17, 4
	call show_char

	; sekundy 2
	mov r16, r24
	ldi r17, 5
	call show_char

	; šestnáctiny / desetiny
	ldi r17, 6

	cpi r25, 0x3A ; >= 10, oprava zobrazení
	brsh show_stopky_alt

	mov r16, r25
	call show_char

ret

show_stopky_alt:
	ldi r16, '9'
	call show_char
ret

; Inicializace joysticku
init_joy:
    in r17, DDRE
    andi r17, 0b11110011
    in r16, PORTE
    ori r16, 0b00001100
    out DDRE, r17
    out PORTE, r16
    ldi r16, 0b00000000
    sts DIDR1, r16
    in r17, DDRB
    andi r17, 0b00101111
    in r16, PORTB
    ori r16, 0b11010000
    out DDRB, r17
    out PORTB, r16
ret

; Ulozi smer joysticku do r1
read_joy:
    push r16 ; zaloha
    push r17
	push r18
	push r19

; Nacte hodnotu a pocka
joy_reread:
    in r16, PINB
	in r18, PINE

    ldi r17, 255
	mov r2, r17

joy_wait:
	dec r2
    brne joy_wait

	; Znovunacteni
    in r17, PINB
	in r19, PINE

	; Ruzne hodnoty = znovunacteni
    cp r16, r17
    brne joy_reread
	cp r18, r19
	brne joy_reread

	; výchozí hodnota: 0 = stopky nebìží, nic není aktivní
	ldi r17, 0
	mov r1, r17

	; enter = reset
	andi r16, 0b00010000
    cpi r16, 0 ; je stisknuty enter?
    breq joy_enter ; ano

	; vlevo = stop
	mov r16, r18
	andi r18, 0b00000100
    cpi r18, 0 ; je stisknute tlacitko vlevo?
    breq joy_left ; ano

	; vpravo = start
	andi r16, 0b00001000
    cpi r16, 0 ; je stisknute tlacitko vpravo?
    breq joy_right ; ano

	jmp joy_end ; obnova registru

joy_enter:
	ldi r17, 1
	mov r1, r17
	jmp joy_end

joy_left:
	ldi r17, 2
	mov r1, r17
	jmp joy_end

joy_right:
	ldi r17, 3
	mov r1, r17
	jmp joy_end ; lehce zbytecne, pro prehlednost kodu...

joy_end:
	pop r19
	pop r18
	pop r17
	pop r16
ret
