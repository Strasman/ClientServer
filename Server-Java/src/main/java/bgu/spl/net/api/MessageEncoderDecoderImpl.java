package bgu.spl.net.api;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.messages.*;
import bgu.spl.net.api.bidi.messages.Error;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private short opcode = -1;
    private String userName = null;
    private String password = null;
    private String birthday = null;
    private short captcha = -1;
    private short follow = -1;
    private String content = null;
    private String dateAndTime = null;
    private String listOfUserNames = null;
    private int spaces = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if(nextByte != ';') {
            if (opcode == -1) {
                bytes[len++] = nextByte;
                if (len == 2) {
                    opcode = bytesToShort(bytes);
                    System.out.println("opcode  " + opcode);
                    len = 0;
                    if (opcode != 3 && opcode!= 7)
                        return null;
                }
            }
            if (opcode == 1 || opcode == 2 || opcode == 6 || opcode == 12) { //userName first
                if (nextByte != '\0') {
                    pushByte(nextByte);
                    return null;
                } else
                    spaces++;
                if (spaces == 1) {
                    userName = popString();
                    System.out.println("username " + userName);
                } else if (spaces == 2) {
                    if (opcode == 6) {
                        content = popString();
                        System.out.println("content " + content);
                    } else {
                        password = popString();
                        System.out.println("password " + password);
                    }
                } else if(spaces == 3){
                    if (opcode == 6) {
                        dateAndTime = popString();
                        System.out.println("dateAndTime " + content);
                    } else if(opcode == 1) {
                        birthday = popString();
                    }
                    else {
                        captcha = Short.parseShort(popString());
                    }
                    }
                }
            if (opcode == 4 ) { //follow \ unfollow message
                if (nextByte != '\0') {
                    pushByte(nextByte);
                    return null;
                } else
                    spaces++;
                if (spaces == 1) {
                    follow = Short.parseShort(popString());
                    System.out.println("follow or unfollow" + follow);
                } else if (spaces == 2) {
                    userName = popString();
                    System.out.println("username " + userName);
                }
            }
            if (opcode == 5 ) { //Post request
                if (nextByte != '\0') {
                    pushByte(nextByte);
                    return null;
                } else
                    spaces++;
                if (spaces == 1) {
                    content = popString();
                    System.out.println("content" + content);
            }
            }
            if (opcode == 8 ) { //Stat
                if (nextByte != '\0') {
                    pushByte(nextByte);
                    return null;
                } else
                    spaces++;
                if (spaces == 1) {
                    listOfUserNames = popString();
                    System.out.println("listOfUserNames" + listOfUserNames);
                }
            }

        }
        else {
            switch (opcode) {
                case 1:
                    return new Register(userName, password, birthday);
                case 2:
                    return new Login(userName, password, captcha);
                case 3:
                    return new Logout();
                case 4:
                    return new Follow(follow, userName);
                case 5:
                    return new Post(content);
                case 6:
                    return new PrivateMessage(userName, content, dateAndTime);
                case 7:
                    return new Logstat();
                case 8:
                    return new Stat(listOfUserNames);
                case 12:
                    return new Block(userName);
            }
        }
        return null;
    }

    @Override
    public byte[] encode(Message message) {
        opcode = message.getOpcode();
        byte[] currOpcode = shortToBytes(opcode);
        byte[] output;
        String optional = message.getOptionalString();
        if (message instanceof Error) {
            byte[] ERRopcode = shortToBytes((short) 11);
            output = merge(ERRopcode, currOpcode);
        }
        else if(message instanceof Ack) {  //ACK message
            byte[] ACKopcode = shortToBytes((short) 10);
            output = merge(ACKopcode, currOpcode);
            byte[] optionalArray = new byte[0];

            if (opcode == 7 || opcode == 8 ) { //(logStat \ stat )  has more info to send
                if (optional != null) {
                    Short[][] logStat = message.getOptionalShortArray();
                    output = merge(ACKopcode, currOpcode);
                    for (int i = 0; i <= logStat.length; i++) {
                        for (int j = 0; j < 4; j++) {
                            output = merge(output, shortToBytes(logStat[i][j]));
                        }
                    }
                }
            }
            if(opcode == 4){ //follow  has more info to send
                if (optional != null)
                    optionalArray = optional.getBytes(StandardCharsets.UTF_8);
                output = merge(output, optionalArray);
            }
        }
        else{ //NOTIFICATION msg
            byte[] NOTIFICATIONopcode = shortToBytes((short) 9);
            byte[] optinalArray= (message.getOptionalString()).getBytes(StandardCharsets.UTF_8);
            output = merge(NOTIFICATIONopcode , currOpcode);
            output = merge(output , optinalArray);
        }
        // for restarting the data saved
        opcode = -1;
        captcha = -1;
        follow = -1;
        len = 0;
        spaces = 0;
        bytes = new byte[1 << 10];
        return output;
    }

    private byte[] merge ( byte[] first, byte[] second){
        int length = first.length + second.length;
        byte[] output = new byte[length];
        System.arraycopy(first, 0, output, 0, first.length);
        if (length - first.length >= 0)
            System.arraycopy(second, 0, output, first.length, length - first.length);
        return output;
    }



    public short bytesToShort(byte[] byteArr) //given code
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num) //given code
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private String popString () { //code from Session 10 in class
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }

    private void pushByte ( byte nextByte){ //code from Session 10 in class
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

}
