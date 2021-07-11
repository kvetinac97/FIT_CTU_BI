CXX = g++
CXXFLAGS = -g -std=c++14
LDFLAGS = build/common.o -lcrypto

all: build/encrypt build/decrypt keys
run: all
	./test.sh
build/encrypt: build/encrypt.o build/common.o
	$(CXX) -o $@ $< $(LDFLAGS)
build/decrypt: build/decrypt.o build/common.o
	$(CXX) -o $@ $< $(LDFLAGS)
keys: build/priv.pem build/pub.pem
build/priv.pem:
	openssl genrsa -out build/priv.pem 2048
build/pub.pem: build/priv.pem
	openssl rsa -in build/priv.pem -pubout -out build/pub.pem
%.o: ../%.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS)
clean:
	rm build/* dst/*.dec dst/*.enc utils/random* utils/helper