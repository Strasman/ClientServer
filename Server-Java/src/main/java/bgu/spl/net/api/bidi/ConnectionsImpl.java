package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.messages.Notification;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionsImpl<T> implements Connections<T> {

    private final ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionHandlersMap;
    private final ConcurrentHashMap<Integer, User> usersMapByID;
    private final ConcurrentHashMap<String, User> usersMapByName;
    private final LinkedBlockingQueue<String> filteredWords;

    public ConnectionsImpl() {
        usersMapByID = new ConcurrentHashMap<>();
        usersMapByName = new ConcurrentHashMap<>();
        connectionHandlersMap = new ConcurrentHashMap<>();
        filteredWords = new LinkedBlockingQueue<>();
        filteredWords.add("war");
    }


    @Override
    public boolean send(int connectionId, T msg) {
        User user = getUserById(connectionId);
        if (user.isLogin()){
            connectionHandlersMap.get(connectionId).send(msg);
            return true;
        }
        else {
            user.setAwaitingNotification((Notification) msg);
            return false;
        }
    }

    @Override
    public void broadcast(T msg) {
    }

    @Override
    public void disconnect(int connectionId) {
        connectionHandlersMap.remove(connectionId);
    }

    public void addUser(String userName, User user) {
        usersMapByName.put(userName,user);
        usersMapByID.put(user.getId(), user);
        //add connection handler to map as well?????
    }

    public boolean isRegistered(String userName) {
        return usersMapByName.containsKey(userName);
    }

    public boolean isEmpty(){return (usersMapByID.isEmpty());}

    public User getUserByName(String userName) {
        return usersMapByName.get(userName);
    }

    public User getUserById(int id) {
        return usersMapByID.get(id);
    }

    public short[][] logStats(){
        short[][] stats = new short[usersMapByID.size()][8];
        int i = 0;
        for (User user : usersMapByID.values()){
            stats[i][0]  = user.getAge();
            stats[i][1] = user.getNumOfPosts();
            stats[i][2] = user.getNumOfFollowers();
            stats[i][3] = user.getNumOfFollowing();

        }
        return stats;
    }

    public short[][] stats(LinkedList<String> namesOfUsers){
        short[][] stats = new short[namesOfUsers.size()][8];
        int i = 0;
        for (String name: namesOfUsers) {

            stats[i][0]  = usersMapByName.get(name).getAge();
            stats[i][1] = usersMapByName.get(name).getNumOfPosts();
            stats[i][2] = usersMapByName.get(name).getNumOfFollowers();
            stats[i][3] = usersMapByName.get(name).getNumOfFollowing();
        }
        return stats;
    }

    public LinkedBlockingQueue<String> getFilteredWords() {
        return filteredWords;
    }

    public void addHandler(int id, ConnectionHandler<T> connectionHandler) {
        connectionHandlersMap.put(id,connectionHandler);
    }

    public void removeHandler(int id) {
        connectionHandlersMap.remove(id);
    }
}


