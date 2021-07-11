CXX = g++
CXXFLAGS = -g -std=c++14
LDFLAGS = -lcrypto -L/usr/local/opt/openssl/lib -I/usr/local/opt/openssl/include

all: hash
run: all
	./hash fldajkfljads; for i in {-1..25}; do ./hash $$i; done
hash: hash.o
	$(CXX) -o $@ $< $(LDFLAGS)
%.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS)
clean:
	rm hash.o hash