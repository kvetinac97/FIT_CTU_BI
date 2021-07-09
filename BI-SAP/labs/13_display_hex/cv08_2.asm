; definice pro nas typ procesoru
.include "m169def.inc"
; podprogramy pro praci s displejem
.org 0x1000
.include "print.inc"

; Zacatek programu - po resetu
.org 0
jmp start

; Zacatek programu - hlavni program
.org 0x100
start:
    ; Inicializace zasobniku
	ldi r16, 0xFF
	out SPL, r16
	ldi r16, 0x04
	out SPH, r16
    ; Inicializace displeje
	call init_disp

	; Nacteme pozadovane cislo
	ldi R22, 0x49

	; Ziskame levou cislici (vrchni 4 bity)
	mov r23, r22
	lsr r23
	lsr r23
	lsr r23
	lsr r23	

	; Porovname a vypiseme na zaklade vysledku
	cpi r23, 0x0A
	brsh isch1
	brlo isnum1
	
isnum1:	ldi r16, '0'
		add r16, r23
		rjmp next1;

isch1:	ldi r16, 'A'
		ldi r24, 0x0A
		sub r23, r24
		add r16, r23

next1:
	ldi r17, 2      ; pozice (zacinaji od 2)
	call show_char  ; zobraz znak

	; Ziskame pravou cislici (dolni 4 bity)
	mov r23, r22
	lsl r23
	lsl r23
	lsl r23
	lsl r23
	lsr r23
	lsr r23
	lsr r23
	lsr r23


	; Porovname a vypiseme na zaklade vysledku
	cpi r23, 0x0A
	brsh isch2
	brlo isnum2
	
isnum2:	ldi r16, '0'
		add r16, r23
		rjmp next2;

isch2:	ldi r16, 'A'
		ldi r24, 0x0A
		sub r23, r24
		add r16, r23

next2:
	ldi r17, 3      ; pozice (zacinaji od 2)
	call show_char  ; zobraz znak


	; Zastavime program - nekonecna smycka
	rjmp PC
