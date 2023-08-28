package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Register extends Message {

    private short opcode;
    private String userName;
    private String password;
    private String birthday;

    public Register(String userName, String password, String birthday) {
        this.opcode = 1;
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
    }


    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        if (connections.isRegistered(userName))
            return new Error((short) 1);
        else {
            User user = new User(userName, password,birthday);
            connections.addUser(userName,user);
            user.setIsRegister();
            return new Ack((short) 1, null);
        }
    }

    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public String getOptionalString() {
        return null;
    }

    @Override
    public Short[][] getOptionalShortArray() {
        return null;
    }


}
