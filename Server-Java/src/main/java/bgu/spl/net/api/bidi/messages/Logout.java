package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Logout extends Message {

    private final short opcode;

    public Logout() {
        this.opcode = 3;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        if (user.isRegister()) {
            if (user.isLogin()) {
                user.setIsLogin();
                //connections.removeHandler(user.getId());
                protocol.setShouldTerminate(true);
                return new Ack((short) 3, null);
            }
        }
        return new Error((short) 3);
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
