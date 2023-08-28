package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Block extends Message {

    private final short opcode;
    private String userName;

    public Block(String userName) {
        this.opcode = 12;
        this.userName = userName;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        User otherUser = connections.getUserByName(userName);
        if (user.isRegister() && otherUser.isRegister()) {
            if (user.isLogin()) {
                if (!user.userIsBlocked(otherUser))
                    user.addBlockedUser(otherUser);
                if ((user.getFollowingList()).containsKey(userName)) {
                    user.removeFromFollowing(otherUser);
                    user.setNumOfFollowing(false);
                    otherUser.removeFromFollowers(user);
                    otherUser.setNumOfFollowers(false);
                }
                return new Ack((short) 12, userName);
            }
        }
        return new Error((short) 12);
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
