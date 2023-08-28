package bgu.spl.net.api.bidi;

import bgu.spl.net.api.bidi.messages.Notification;
import bgu.spl.net.api.bidi.messages.Post;
import bgu.spl.net.api.bidi.messages.PrivateMessage;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class User {

    private final String userName;
    private final String password;
    private final short age;
    private boolean isRegister;
    private boolean isLogin;
    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;
    private int id;
    private ConcurrentHashMap<String, User> followersList;
    private ConcurrentHashMap<String, User> followingList;
    private LinkedBlockingQueue<Post> posts;
    private LinkedBlockingQueue<PrivateMessage> privateMessages;
    private LinkedBlockingQueue<User> blockedUsers;
    //private ConcurrentHashMap<User, String> timeStampPerUser;
    private LinkedBlockingQueue<Notification> awaitingNotification;

    public User(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        isRegister = false;
        isLogin = false;
        numOfFollowers = 0;
        numOfFollowing = 0;
        numOfPosts = 0;
        //convert the birthday date to the user's age
        int day = Integer.parseInt(birthday.substring(0,2));
        int month = Integer.parseInt(birthday.substring(3,5));
        int year = Integer.parseInt(birthday.substring(6));
        LocalDate dateOfBirth = LocalDate.of(year,month,day);
        LocalDate today = LocalDate.now();
        age = (short) Period.between(dateOfBirth,today).getYears();
        id = -1;
        followersList = new ConcurrentHashMap<>();
        followingList = new ConcurrentHashMap<>();
        posts = new LinkedBlockingQueue<>();
        privateMessages = new LinkedBlockingQueue<>();
        blockedUsers = new LinkedBlockingQueue<>();
        //timeStampPerUser = new ConcurrentHashMap<>();
        awaitingNotification = new LinkedBlockingQueue<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public short getAge() {
        return age;
    }

    public short getNumOfPosts() {
        return numOfPosts;
    }

    public void setNumOfPosts() {
        numOfPosts++;
    }

    public short getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(boolean add) {
        if (add)
            numOfFollowers++;
        else
            numOfFollowers--;
    }

    public short getNumOfFollowing() {
        return numOfFollowing;
    }

    public void setNumOfFollowing(boolean add) {
        if (add)
            numOfFollowing++;
        else
            numOfFollowing--;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setIsRegister() {
        isRegister = !isRegister;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin() {
        isLogin = !isLogin;
    }

    public ConcurrentHashMap<String,User> getFollowersList() {
        return followersList;
    }

    public ConcurrentHashMap<String,User> getFollowingList() {
        return followingList;
    }

    public void addToFollowers(User user) {
        followersList.put(user.getUserName(), user);
    }

    public void removeFromFollowers(User user) {
        followersList.remove(user.getUserName());
    }

    public void addToFollowing(User user) {
        followingList.put(user.getUserName(), user);
    }

    public void removeFromFollowing(User user) {
        followersList.remove(user.getUserName());
    }

    public LinkedBlockingQueue<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        posts.add(post);
        numOfPosts++;
    }

    public LinkedBlockingQueue<PrivateMessage> getPrivateMessages() {
        return privateMessages;
    }

    public void addPrivateMessage(PrivateMessage privateMessage) {
        privateMessages.add(privateMessage);
    }

    public boolean userIsBlocked(User user){
        return (blockedUsers.contains(user));
    }

    public void addBlockedUser(User user){
        blockedUsers.add(user);
    }

    public LinkedBlockingQueue<Notification> getAwaitingNotification() {
        return awaitingNotification;
    }

    public void  setAwaitingNotification(Notification n ) {
        awaitingNotification.add(n);
    }

    //    public ConcurrentHashMap<User, String> getTimeStampPerUser() {
//        return timeStampPerUser;
//    }

}
