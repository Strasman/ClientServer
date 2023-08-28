package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Stat extends Message {

    private final short opcode;
    private String listOfUsernames;
    private LinkedList<String> namesOfUsers;

    public Stat(String listOfUsernames) {
        this.opcode = 8;
        this.listOfUsernames = listOfUsernames;
        int i = 0;
        while(i < listOfUsernames.length()){
            String name = "";
            if(listOfUsernames.indexOf(i)!='|')
                name += listOfUsernames.valueOf(i);
            else
                namesOfUsers.add(name);
        }
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        if (user.isRegister()) {
            if (user.isLogin()) {
                return new Ack((short) 8, connections.stats(namesOfUsers));
            }
        }
        return new Error((short) 8);
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
