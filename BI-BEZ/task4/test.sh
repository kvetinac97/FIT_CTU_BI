#!/usr/bin/bash

# Step 1: Fixed data tests
cd src
for FILE in *.bin
do
  # Define output
  ENC_FILE="../dst/${FILE/.bin/.enc}"
  DEC_FILE="../dst/${FILE/.bin/.dec}"
  # Perform test
  ../build/encrypt "$FILE" "$ENC_FILE" "../build/pub.pem"
  ../build/decrypt "$ENC_FILE" "$DEC_FILE" "../build/priv.pem"
  cmp "$FILE" "$DEC_FILE" && echo "Pass: Files are same" || echo "Fail: Files are different"
done

echo "Running random tests, please wait..."

PASS=0
FAILS=0

# Step 2: Random test
cd ..
gcc -Wall -pedantic utils/helper.c -o utils/helper
for i in {1..100}
do
  # Generate random file
  BYTES=$(($RANDOM % (i * 100)))
  utils/helper utils/random.bin "$BYTES"
  # Perform random test
  build/encrypt utils/random.bin utils/random.enc build/pub.pem 1>/dev/null 2>&1
  build/decrypt utils/random.enc utils/random.dec build/priv.pem 1>/dev/null 2>&1
  cmp utils/random.bin utils/random.dec && PASS=$((PASS + 1)) || FAILS=$((FAILS + 1))
done

echo "Total passed: $PASS, failed: $FAILS"
