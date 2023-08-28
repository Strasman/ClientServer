//
// Created by Rony on 1/1/2022.
//

#include <thread>
#include "../include/ServerReader.h"


ServerReader::ServerReader(ConnectionHandler &connectionHandler1) : connectionHandler(connectionHandler1) {}

void ServerReader::run() {
    while (connectionHandler.isLogin()) {
        char opCodeArr[2];
        if (!connectionHandler.getBytes(opCodeArr, 2)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        short opCode = bytesToShort(opCodeArr);
        if (opCode == 10) {  //ACK msg received
            if (!connectionHandler.getRecivedMessage()) {
                decodeACK();
            }
        }
        else if (opCode == 11) {  //ERR msg received
            if (!connectionHandler.getRecivedMessage()) {
                decodeERR();
            }
        }
        else if(opCode == 9){ //notification msg received
            if (!connectionHandler.getRecivedMessage()) {
                decodeNotification();
            }
        }
    }
}

short bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void ServerReader::decodeACK() {
    char opCodeACKArr[2];
    if (!connectionHandler.getBytes(opCodeACKArr, 2)) {
        std::cout << "Disconnected. Exiting...\n" << std::endl;
        return;
    }
    short opcodeACK = bytesToShort(opCodeACKArr);
    if (opcodeACK == 4) //follow additional info to print
        follow();
    else if (opcodeACK == 7 || opcodeACK == 8) //stats additional info to print
        stats();
    else
        cout << "ACK" + to_string(opcodeACK) << endl;
    connectionHandler.setRecivedMessage(true);
}


void ServerReader::follow() { //for specific replay to follow
    string follow;
    char currByte;
    while (currByte != '\0') {
        connectionHandler.getBytes(&currByte, 1);
        follow += currByte;
    }
    cout <<  "ACK " + follow << endl;
}


void ServerReader::stats() { //for specific replay to logstat / stat
    string age1;
    string numPosts1;
    string numFollowers1;
    string numFollowing1;
    char currByte;
    cout << "ACK ";
    while (currByte != '*') {
            connectionHandler.getBytes(&currByte, 2);
            age1 += currByte;
            connectionHandler.getBytes(&currByte, 2);
            numPosts1 += currByte;
            connectionHandler.getBytes(&currByte, 2);
            numFollowers1 += currByte;
            connectionHandler.getBytes(&currByte, 2);
            numFollowing1 += currByte;
            cout << age1 + " " +  numPosts1 + " " + numFollowers1 + " " + numFollowing1 << endl;
    }

}

void ServerReader::decodeERR() { //error replay
    char opCodeERRArr[2];
    if (!connectionHandler.getBytes(opCodeERRArr, 2)) {
        std::cout << "Disconnected. Exiting...\n" << std::endl;
        return;
    }
    short ERRopcode = bytesToShort(opCodeERRArr);
    cout << "ERROR" + to_string(ERRopcode) << endl;
    connectionHandler.setRecivedMessage(true);
}

void ServerReader::decodeNotification() { //for decoding PM msg / Post msg
    char opCodeACKArr[2];
    char type;
    string postingUser;
    string content;
    string pmMsg = "PM";
    string publicMsg = "Public";
    bool postMsg = false;
    char currByte;
    while (currByte != '*') {
        connectionHandler.getBytes(&currByte, 1);
        type += currByte;
        postMsg = (type == '1'); 
        while (currByte != '\0') {
            connectionHandler.getBytes(&currByte, 1);
            postingUser += currByte;
        }
        while (currByte != '\0') {
            connectionHandler.getBytes(&currByte, 1);
            content += currByte;
        }
    }
    if(postMsg)
        cout << "NOTIFICATION " + publicMsg + " " +  postingUser + " " + content << endl;
    else
        cout << "NOTIFICATION " + pmMsg + postingUser + " " +  content << endl;
}


