//
// Created by Rony on 1/1/2022.
//

#ifndef ASSIGNMENT3SPLCPP_SERVERREADER_H
#define ASSIGNMENT3SPLCPP_SERVERREADER_H


#include "../include/KeyboardReader.h"
#include "../include/connectionHandler.h"
using namespace std;

class ServerReader {
private:
    ConnectionHandler &connectionHandler;
    short bytesToShort(char* bytesArr);
    void decodeACK();
    void decodeERR();
    void decodeNotification();
    void follow();
    void isRegister();
    void stats();



public:
    ServerReader(ConnectionHandler &connectionHandler);
    void run();

};

#endif //ASSIGNMENT3SPLCPP_SERVERREADER_H

