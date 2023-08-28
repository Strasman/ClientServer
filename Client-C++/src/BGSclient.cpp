#include <iostream>
#include <thread>
#include "../include/connectionHandler.h"
#include "../include/KeyboardReader.h"
#include "../include/ServerReader.h"


int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler ch(host, port);
    if (!ch.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    KeyboardReader KB (ch);
    thread keyboardReaderThread(&KeyboardReader::run, KB);
    ServerReader SR(ch);
    SR.run();
    keyboardReaderThread.join();
    return 0;
}