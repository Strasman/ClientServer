package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;

public class Follow extends Message {

    private final short opcode;
    private short follow;
    private String userName;


    public Follow(short follow, String userName) {
        this.opcode = 4;
        this.follow = follow; //0 for follow, 1 for unFollow
        this.userName = userName;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        User otherUser = connections.getUserByName(userName);
        if (user != null && otherUser != null && user.isRegister() && otherUser.isRegister()) {
            if (user.isLogin() && !user.userIsBlocked(otherUser) && !otherUser.userIsBlocked(user)) {
                if (follow == 0) { //follow action
                    if (!(user.getFollowingList()).containsKey(userName)) {
                        user.addToFollowing(otherUser);
                        user.setNumOfFollowing(true);
                        otherUser.addToFollowers(user);
                        otherUser.setNumOfFollowers(true);
                        return new Ack((short) 4, userName);
                    }
                }
                else if (follow == 1) { //unfollow action
                    if ((user.getFollowingList()).containsKey(userName)) {
                        user.removeFromFollowing(otherUser);
                        user.setNumOfFollowing(false);
                        otherUser.removeFromFollowers(user);
                        otherUser.setNumOfFollowers(false);
                        return new Ack((short) 4, userName);
                    }
                }
            }
        }
        return new Error((short) 4);
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
