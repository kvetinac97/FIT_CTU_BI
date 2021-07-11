CXX = g++
CXXFLAGS = -g -std=c++14
LDFLAGS = -lcrypto -L/usr/local/opt/openssl/lib -I/usr/local/opt/openssl/include

all: main
run: all
	./test.sh
main: main.o
	$(CXX) -o $@ $< $(LDFLAGS)
%.o: %.cpp
	$(CXX) -c -o $@ $< $(CXXFLAGS)
clean:
	rm main.o main dst/*