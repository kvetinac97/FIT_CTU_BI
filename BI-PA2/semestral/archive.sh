mkdir -p wrzecond

cp zadani.txt wrzecond/zadani.txt
cp prohlaseni.txt wrzecond/prohlaseni.txt
cp Makefile wrzecond/Makefile
cp Doxyfile wrzecond/Doxyfile

mkdir -p wrzecond/src
cp -r src/ wrzecond/src/
mkdir -p wrzecond/examples
cp -r examples/ wrzecond/examples/

zip -r wrzecond.zip wrzecond
rm -rf wrzecond/