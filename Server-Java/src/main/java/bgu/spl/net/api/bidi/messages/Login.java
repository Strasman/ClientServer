package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.User;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Login extends Message {

    private final short opcode;
    private String userName;
    private String password;
    private short captcha;

    public Login(String userName, String password, short captcha) {
        this.opcode = 2;
        this.userName = userName;
        this.password = password;
        this.captcha = captcha;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        ConnectionsImpl<T> connections = protocol.getConnection();
        if (connections.isRegistered(userName)) {
            User user = connections.getUserByName(userName);
            if (user.isLogin() || user.getPassword() != password || captcha == 0) {
                return new Error((short) 2);
            }
            else {
                user.setIsLogin();
                LinkedBlockingQueue<Notification> messages = user.getAwaitingNotification();
                for (Notification n : messages) {
                    connections.send(user.getId(), (T) n);
                }
                return new Ack((short) 2, null);
            }
        }
        else {
            return new Error((short) 2);
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
