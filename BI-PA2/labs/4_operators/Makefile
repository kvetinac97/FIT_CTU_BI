.PHONY: all
all: bin/main

bin/CDouble.o: CDouble.cpp CDouble.hpp
	mkdir -p bin
	g++ -Wall -pedantic -std=c++14 -g -fsanitize=address -c -o bin/CDouble.o CDouble.cpp

bin/CMod.o: CMod.cpp CMod.hpp
	mkdir -p bin
	g++ -Wall -pedantic -std=c++14 -g -fsanitize=address -c -o bin/CMod.o CMod.cpp

bin/CSet.o: CSet.cpp CSet.hpp CMod.hpp
	mkdir -p bin
	g++ -Wall -pedantic -std=c++14 -g -fsanitize=address -c -o bin/CSet.o CSet.cpp

bin/main.o: main.cpp CMod.hpp CSet.hpp CDouble.hpp
	mkdir -p bin
	g++ -Wall -pedantic -std=c++14 -g -fsanitize=address -c -o bin/main.o main.cpp

bin/main: bin/main.o bin/CMod.o bin/CSet.o bin/CDouble.o
	mkdir -p bin
	g++ -Wall -pedantic -std=c++14 -g -fsanitize=address -o bin/main bin/main.o bin/CMod.o bin/CSet.o bin/CDouble.o


.PHONY: run
run: bin/main
	bin/main


.PHONY: clear
clear:
	rm -r bin/ 2>/dev/null
