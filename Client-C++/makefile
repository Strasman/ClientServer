CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread -lboost_thread

all: bin/BGSclient
	g++ -o bin/KeyboardReader.o bin/connectionHandler.o bin/ServerReader.o $(LDFLAGS)

	@echo 'Building target: BGRSclient'
	@echo 'Invoking: C++ Linker'

bin/BGSclient: bin/ServerReader.o bin/connectionHandler.o bin/KeyboardReader.o
	g++ -Wall -Weffc++ -o bin//BGSclient bin/ServerReader.o bin/ConnectionHandler.o bin/KeyboardReader.o  $(LDFLAGS)
    @echo 'Finished building target: Client'


bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp $(LDFLAGS)

bin/ServerReader.o: src/ServerReader.cpp
	g++ $(CFLAGS) -o bin/ServerReader.o src/ServerReader.cpp

bin/KeyboardReader.o:src/KeyboardReader.cpp
	g++ $(CFLAGS) -o bin/KeyboardReader.o src/KeyboardReader.cpp

.PHONY: clean
clean:
	@echo 'Cleaning'
	rm -f bin/*



.PHONY: clean
clean:
	rm -f bin/*
