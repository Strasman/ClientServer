//
// Created by Rony on 1/1/2022.
//

#ifndef ASSIGNMENT3SPLCPP_KEYBOARDREADER_H
#define ASSIGNMENT3SPLCPP_KEYBOARDREADER_H

#include "../include/connectionHandler.h"
#include <vector>

using namespace std;

class KeyboardReader {

private:
    ConnectionHandler &connectionHandler;
    string message;
    short opcode;
    string userName;
    string password;
    string addition;
    string content;
    string dateAndTime;
    char* bytes;
    vector<char> bytesChar;
    void shortToBytes(short num, char* bytesArr);
    vector<string> split(string line, string delimiter);
    void merge(vector<char> a);
    void encode(string line);

public:
    KeyboardReader(ConnectionHandler &connectionHandler);
    void run();
};


#endif //ASSIGNMENT3SPLCPP_KEYBOARDREADER_H
