#!/bin/bash

gcc -Wall -pedantic -g -fsanitize=address progtest.c -o progtest
chmod +x ./progtest

for file in CZE/*_in.txt
 do
  refName=${file/in.txt/ref.txt}
  outName=${file/in.txt/out.txt}

  ./progtest < $file > $refName
  if ! diff "$refName" "$outName" ; then
    echo "Error in test file $file"
    exit 1
  fi
 done
