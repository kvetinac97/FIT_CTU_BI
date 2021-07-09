;Naètení konstant
LDI R16, 5
LDI R17, 10
LDI R18, 58
;R16 * 4
LSL R16
LSL R16
;R17 * 3 = R17 + (R17 * 2)
MOV R19, R17
LSL R17
ADD R17, R19
;R17 - R19
SUB R17, R18
;R16 + R17
ADD R16, R17
;Výsledek / 8
ASR R16
ASR R16
ASR R16
;Pøesunutí do výsledného registru
MOV R20, R16
;Výsledek
END: JMP END
