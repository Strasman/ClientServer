package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Logstat extends Message {

    private final short opcode;

    public Logstat() {
        this.opcode = 7;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        if (user.isRegister()) {
            if (user.isLogin()) {
                return new Ack((short) 7, connections.logStats());
            }
        }
        return new Error((short) 7);
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
