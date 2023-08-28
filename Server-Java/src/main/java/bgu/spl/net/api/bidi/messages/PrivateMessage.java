package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class PrivateMessage extends Message {

    private final short opcode;
    private String userName;
    private String content;
    private String dateAndTime;

    public PrivateMessage(String userName, String content, String dateAndTime) {
        this.opcode = 6;
        this.userName = userName;
        this.content = content;
        this.dateAndTime = dateAndTime;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        User otherUser = connections.getUserByName(userName);
        LinkedBlockingQueue<String> toFilter = connections.getFilteredWords();
        if (user != null && otherUser != null && user.isRegister() && otherUser.isRegister()) {
            if (user.isLogin() && !otherUser.userIsBlocked(user) && user.getFollowingList().containsKey(otherUser.getUserName())) {
                for (String s : toFilter) {
                    content = content.replaceAll(s, "<filtered>");
                }
                Notification notification = new Notification((short) 0, user.getUserName(), content);
                connections.send(otherUser.getId(), (T)notification);
                user.addPrivateMessage(this);
                return new Ack((short) 6, null);
            }
        }
        return new Error((short) 6);
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
