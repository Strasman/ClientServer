package bgu.spl.net.api.bidi;
import bgu.spl.net.api.bidi.Message;


public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate = false;
    private User user;
    private ConnectionsImpl<T> connection;
    private int id;

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.id = connectionId;
        this.connection = (ConnectionsImpl<T>) connections;
    }


    @Override
    public void process(Message message) {
        T response = (T) message.process(this);
        connection.send(id,response);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void setShouldTerminate(boolean change) {
        shouldTerminate = change;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConnectionsImpl<T> getConnection() {
        return connection;
    }


    public int getId() {
        return id;
    }
}
