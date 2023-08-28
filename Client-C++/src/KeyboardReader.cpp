//
// Created by Rony on 1/1/2022.
//

#include "../include/KeyboardReader.h"

    KeyboardReader::KeyboardReader(ConnectionHandler &handler) : connectionHandler(handler),message(),opcode(-1),userName(),password(),addition(){}

    void KeyboardReader::run() {
        while (connectionHandler.isLogin()) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            int len = line.length();
            encode(line);
        }
    }

    void KeyboardReader::encode(string line) {
        vector<string> splitLine = split(line, " ");
        message = splitLine[0];

        if (message == "REGISTER" | message == "LOGIN") {
            if (message == "REGISTER")
                opcode = 1;

            else
                opcode = 2;
            userName = splitLine[1];
            password = splitLine[2];
            addition = splitLine[3];
            vector<char> bytesArrUserName;
            vector<char> bytesArrPassword;
            vector<char> bytesArrAddition;
            shortToBytes(opcode, bytes);
            bytesChar.push_back(bytes[0]);
            bytesChar.push_back(bytes[1]);
            for (size_t i = 0; i < userName.size(); ++i) {
                bytesArrUserName.push_back(userName.at(i));
            }
            bytesArrUserName.push_back('0');
            for (size_t i = 0; i < password.size(); ++i) {
                bytesArrPassword.push_back(password.at(i));
            }
            bytesArrPassword.push_back('0');
            for (size_t i = 0; i < addition.size(); ++i) {
                bytesArrAddition.push_back(addition.at(i));
            }
            bytesArrAddition.push_back('0');
            merge(bytesArrUserName);
            merge(bytesArrPassword);
            merge(bytesArrAddition);
        }

        else if (message == "LOGOUT" || message == "LOGSTAT") { //this type of messages has no parameters
            if (message == "LOGOUT") {
                opcode = 3;
            }
            else {
                opcode = 7;
            }
            shortToBytes(opcode, bytes);
            bytesChar.push_back(bytes[0]);
            bytesChar.push_back(bytes[1]);
        }

        else if (message == "FOLLOW") {
            opcode = 4;
            addition = splitLine[1];
            userName = splitLine[2];
            shortToBytes(opcode, bytes);
            bytesChar.push_back(bytes[0]);
            bytesChar.push_back(bytes[1]);
            bytesChar.push_back(addition.at(0));
            vector<char> bytesArrUserName;
            for (size_t i = 0; i < userName.size(); ++i) {
                bytesArrUserName.push_back(userName.at(i));
            }
            merge(bytesArrUserName);
        }
        else if (message == "PM") {
            opcode = 6;
            userName = splitLine[1];
            for (int i = 2; i < splitLine.size(); i++) {
                content.append(splitLine[i]);
                content.append(" ");
            }
            shortToBytes(opcode, bytes);
            bytesChar.push_back(bytes[0]);
            bytesChar.push_back(bytes[1]);
            vector<char> bytesArrUserName;
            for (size_t i = 0; i < userName.size(); ++i) {
                bytesArrUserName.push_back(userName.at(i));
            }
            bytesArrUserName.push_back('0');
            vector<char> bytesArrContent;
            for (size_t i = 0; i < content.size(); ++i) {
                bytesArrContent.push_back(content.at(i));
            }
            bytesArrContent.push_back('0');
            merge(bytesArrUserName);
            merge(bytesArrContent);
        }
        //need to add date and time of today and push it!!!!!!
        else if (message == "POST" || message == "STAT") {
            if (message == "POST")
                opcode = 5;
            else
                opcode = 8;
            for (int i = 2; i < splitLine.size(); i++) {
                content.append(splitLine[i]);
                content.append(" ");
            }            shortToBytes(opcode, bytes);
            bytesChar.push_back(bytes[0]);
            bytesChar.push_back(bytes[1]);
            vector<char> bytesArrContent;
            for (size_t i = 0; i < content.size(); ++i) {
                bytesArrContent.push_back(content.at(i));
            }
            bytesArrContent.push_back('0');
            merge(bytesArrContent);
            }
        else if (message == "BLOCK") {
            opcode = 12;
            shortToBytes(opcode, bytes);
            bytesChar.push_back(bytes[0]);
            bytesChar.push_back(bytes[1]);
            vector<char> bytesArrUserName;
            for (size_t i = 0; i < userName.size(); ++i) {
                bytesArrUserName.push_back(userName.at(i));
            }
            bytesArrUserName.push_back('0');
            merge(bytesArrUserName);
        }
        connectionHandler.sendBytes(&bytesChar[0], bytesChar.size());
        connectionHandler.setRecivedMessage(false);
        bytesChar.clear();
    }

    void shortToBytes(short num, char* bytesArr) {
        bytesArr[0] = ((num >> 8) & 0xFF);
        bytesArr[1] = (num & 0xFF);
    }

    vector<string> KeyboardReader::split(string line, string delimiter) {
        vector<string> output;
        size_t pos = 0;
        string token;
        while ((pos = line.find(delimiter)) != string::npos) {
            token = line.substr(0, pos);
            output.push_back(token);
            line.erase(0, pos + delimiter.length());
        }
        output.push_back(line); //add the last word in the line
        return output;
    }

    void KeyboardReader::merge(vector<char> a) {
        for (int i = 0; i < a.size(); i++)
            bytesChar.push_back(a[i]);
    }