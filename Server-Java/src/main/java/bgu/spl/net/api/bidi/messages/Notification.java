package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Notification extends Message {

    private final short opcode;
    private short type;
    private String postingUser;
    private String content;

    public Notification(short type, String postingUser, String content) {
        this.opcode = 9;
        this.type = type;
        this.postingUser = postingUser;
        this.content = content;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        return null;
//        ConnectionsImpl<T> connections = protocol.getConnection();
//        User user = connections.getUserById(protocol.getId());
//        if (user.isRegister()) {
//            if (user.isLogin()) {
//                String optional = type + postingUser + content;
//                return new Ack((short) 9, optional); //all additional information in optional
//            }
//        }
//        return new Error((short) 9);
    }

    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public String getOptionalString() {
        String output = type + postingUser + '0' + content +'0' + '*';
        return output;
    }

    @Override
    public Short[][] getOptionalShortArray() {
        return null;
    }


}
