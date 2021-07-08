# Vytvoření binárky
if ! [[ -f ../wrzecond ]]
then
  cd ..
  make compile
  cd examples
fi

# Testy
success=0;
failure=0;

for folder in */
  do
    for i in $folder*.in
      do
        if [[ $i == $folder"*.in" ]]
        then
          break;
        fi

        output=${i//.in/.out}
        ref=${i//.in/.ref}

        ../wrzecond assert < "$i" > "$output" 2>/dev/null
        if diff "$output" "$ref"
          then success=$((success + 1))
        else
          failure=$((failure + 1))
          echo "Failed $output"
        fi

      done
      # clear
      rm -f $folder*.out
  done

total=$((success + failure));

echo "\033[32mSuccess\033[m: $success/$total\n\033[31mFailure\033[m: $failure/$total"
