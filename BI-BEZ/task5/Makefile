CXX = gcc
CXXFLAGS = -g
LDFLAGS = -lcrypto -lssl

all: test/main
run: all
	./test.sh
test/main: test/main.o
	$(CXX) -o $@ $< $(LDFLAGS)
%.o: ../%.c
	$(CXX) -c -o $@ $< $(CXXFLAGS)
clean:
	rm -rf test/*