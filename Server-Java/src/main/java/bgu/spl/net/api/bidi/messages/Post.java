package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Post extends Message {

    private final short opcode;
    private String content;
    private LinkedBlockingQueue<User> usersForPost;

    public Post(String content) {
        this.opcode = 5;
        this.content = content;
        usersForPost = new LinkedBlockingQueue<>();
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        User user = connections.getUserById(protocol.getId());
        //LinkedBlockingQueue<String> users = new LinkedBlockingQueue<>();
        if (user != null && user.isRegister()) {
            if (user.isLogin()) {
                ConcurrentHashMap<String,User> followers = user.getFollowersList();
                for (int i = 0; i < content.length(); i++) {
                    if (content.charAt(i) == '@') {
                        String name = "";
                        i++;
                        while (content.charAt(i) != ' ') {
                            name += (content.charAt(i));
                            i++;
                        }
                        User otherUser = connections.getUserByName(name);
                        if (!followers.containsKey(name) && user.userIsBlocked(otherUser) && otherUser.userIsBlocked(user)) { //do not send to blocked
                            usersForPost.add(otherUser);
                        }
                    }
                }
                usersForPost.addAll(followers.values());
                Notification notification = new Notification((short) 1, user.getUserName(), content);
                for (User u: usersForPost) {
                    connections.send(u.getId(), (T)notification);
                }
                user.addPost(this);
                return new Ack((short) 5, null);
            }
        }
        return new Error((short) 5);
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
