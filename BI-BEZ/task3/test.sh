#!/usr/bin/bash

# Check each image file
cd src
for FILE in *.tga
do
  # Define ECB files
  ECB_ENC="../dst/${FILE/.tga/_ecb.tga}"
	ECB_DEC="../dst/${FILE/.tga/_dec.tga}"
	# Perform ECB test
	../main "$FILE" "$ECB_ENC" ECB 1
	../main "$ECB_ENC" "$ECB_DEC" ECB 0
	cmp "$FILE" "$ECB_DEC"
	# Define CBC files
	CBC_ENC="../dst/${FILE/.tga/_cbc.tga}"
	CBC_DEC="../dst/${FILE/.tga/_cbc_dec.tga}"
	# Perform CBC test
	../main "$FILE" "$CBC_ENC" CBC 1
	../main "$CBC_ENC" "$CBC_DEC" CBC 0
	cmp "$FILE" "$CBC_DEC"
done