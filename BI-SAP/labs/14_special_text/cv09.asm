.cseg
.include "m169def.inc"
.org 0x1000
.include "print.inc"

; Zacatek programu - po resetu
.org 0
jmp	  start

.org 0x100

retez: .db "ONDRA JE VELMI ZKUSENY ASSEMBLERISTA A RAD BY DOSTAL 3 BODY ZA TUTO ULOHU",0 ; text, co se má zobrazit
; delka retezce k zobrazeni se nastavuje v registru, viz nize

start:
	; Inicializace zasobniku
	ldi r16, 0xFF
	out SPL, r16
	ldi r16, 0x04
	out SPH, r16

	ldi r30, low(2*retez)
	ldi r31, high(2*retez)

	; Inicializace displeje
	call init_disp
	ldi r21, 6 ; delka displeje

; Jednotlive zobrazovani znaku
run:
	; zapamatujeme si pozici na textu
	mov r0, r30
	mov r1, r31

	mov r19, r21 ; nakopirovani delky displeje
	call showdispl ; nacte displej

	; vratime text na spravnou pozici
	mov r30, r0
	mov r31, r1	

	lpm r16, Z+ ; inkrement retezce

	; pockame
	ldi r22, 10
	; call wait - èekací smyèka
	jmp run ; dalsi beh

showdispl:

	; for ( i = r19; i > 0; i-- )
	cpi r19, 0
	brne showchar

	; navrat
	ret

; zobrazi znak na #delka - r2té pozici
showchar:

	mov r3, r21
	sub r3, r19
	ldi r18, 2
	add r3, r18

	; zobrazime aktualni znak
	lpm r16, Z+

	; konec programu - dosel text
	cpi r16, 0
	breq end

	mov r17, r3      ; pozice (zacinaji od 2)

	call show_char  ; zobraz znak
	dec r19
	jmp showdispl

; cekaci smycka
wait:
	ldi r23, 255
	wait2:
		ldi r24, 255
	wait3:
		dec r24
		brne wait3
		dec r23
	brne wait2
	dec r22

brne wait
ret
;jmp run ; dalsi beh



end: jmp end
