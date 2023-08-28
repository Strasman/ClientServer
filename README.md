# ClientServer
Simple social network server and client.

The communication between the server and the client(s) will be
performed using a binary communication protocol. A registered user will be
able to follow other users and post messages.

The implementation of the server will be based on the Thread-Per-Client
(TPC) and Reactor servers taught in class.
The servers, as seen in class, only support pull notifications.
Any time the server receives a message from a client it can replay back to
the client itself. But what if we want to send messages between clients, or
broadcast an announcement to a group of clients? We would like the server to
send those messages directly to the client without receiving a request to do
so. this behaviour is called push notifications.
The first part of the assignment will be to replace some of the current
interfaces with new interfaces that will allow such a case.

Unlike real social network you will not work with real databases. You will need
to save data (Users, Passwords, Messages, ect...). You only need to save
information from the time the server starts and keep it in memory until the
server closes.

The BGS protocol supports 11 types of messages:
• 1-8 are Client-to-Server messages
• 9-11 are Server-to-Client messages
Opcode Operation
1 Register request (REGISTER)
2 Login request (LOGIN)
3 Logout request (LOGOUT)
4 Follow / Unfollow request
(FOLLOW)
5 Post request (POST)
6 PM request (PM)
7 Logged in States request
(LOGSTAT)
8 Stats request (STAT)
9 Notification (NOTIFICATION)
10 Ack (ACK)
11 Error (ERROR)
12 Block (BLOCK)

2.2 Server
You will have to implement a single protocol, supporting both the Thread-Per-
Client and Reactor server patterns presented in class. Code seen in class for both
servers is included in the assignment wiki page. You are also provided with 3
new or changed interfaces:

• Connections – This interface should map a unique ID for each active client
connected to the server. The implementation of Connections is part of the server
pattern and not part of the protocol. It has 3 functions that you must implement
(You may add more if needed):
- boolean send(int connId, T msg) – sends a message T to client
represented by the given connId
- void broadcast(T msg) – sends a message T to all active clients.
This includes clients that has not yet completed log-in by the
BGS protocol. Remember, Connections belongs to the server
pattern implemenration, not the protocol!.
- void disconnect(int connId) – removes active client connId from
map.

• ConnectionHandler - A function was added to the existing interface. o Void
send(T msg) – sends msg T to the client. Should be used by send and broadcast
in the Connections implementation.

• BidiMessagingProtocol – This interface replacesthe MessagingProtocol
interface. It exists to support peer 2 peer messaging via the Connections
interface. It contains 2 functions:

- void start(int connectionId, Connections connections) – initiate the
protocol with the active connections structure of the server and saves
the owner client’s connection id.

- void process(T message) – As in MessagingProtocol, processes a given
message. Unlike MessagingProtocol, responses are sent via the
connections object send function. Left to you, are the following tasks:

1. Implement Connections to hold a list of the new ConnectionHandler
interface for each active client. Use it to implement the interface
functions. Notice that given a connections implementation, any
protocol should run. This means that you keep your implementation
of Connections on T. public class ConnectionsImpl<T> implements
Connections<T> {…}.

2. Refactor the Thread-Per-Client server to support the new interfaces.
The ConnectionHandler should implement the new interface. Add
calls for the new Connections<T> interface. Notice that the
ConnectionHandler<T> should now work with the
BidiMessagingProtocol<T> interface instead of MessagingProtocol.

3. Refactor the Reactor server to support the new interfaces. The
ConnectionHandler should implement the new interface. Add calls
for the new Connections<T> interface. Notice that the
ConnectionHandler<T> should now work with the
BidiMessagingProtocol<T> interface instead of
MessagingProtocol<T>.

4. Tasks 1 to 3 MUST not be specific for the protocol
implementation. Implement the new BidiMessagingProtocol and
MessageEncoderDecoder to support the BGS protocl as described in
section 1.2. You will also need to define messages( <T> in the
interfaces). You may add more classes as neccesery to implement
the protocol (shared protocol data ect…).

2.3 Client
An echo client is provided, but its a single threaded client. While it is blocking on
stdin (read from keyboard) it does not read messages from the socket. You
should improve the client so that it will run 2 threads. One should read from
keyboard while the other should read from socket. The client should receive the
server’s IP and PORT as arguments. You may assume a network disconnection
does not happen (like disconnecting the network cable). You may also assume
legel input via keyboard.
The client should recive commands using the standard input. Commands are
defined in section 1.2 under command initiation sub sections. You will need to
translate from keyboard command to network messages and the other way
around to fit the specifications.
Notice that the client should close itself upon reception of an ACK message in
response of an outgoing LOGOUT command.
The Client directory should contain a src, include and bin subdirectories and a
Makefile as shown in class. The output executable for the client is named
BGSclient and should reside in the bin folder after calling make.
Testing run commands: BGSclient <ip> <port>

4 Examples

4.1 Registeration and login
Server assumptions for example:
 Server currently has 1 registered user named “Morty” with password
“a123”

CLIENT#1< LOGIN Morty a321
CLIENT#1> ERROR 2
(Failed because of wrong password)
CLIENT#1< LOGIN Rick a123 1
CLIENT#1> ERROR 2
(Failed because username Rick isn’t registered)
CLIENT#1< LOGIN Morty a123 0
CLIENT#1> ERROR 2
(Failed because of captcha 0)
CLIENT#1< LOGIN Morty a123 1
CLIENT#1> ACK 2
CLIENT#2< LOGIN Morty a123 1
CLIENT#2> ERROR 2
(Failed because Morty is already logged-in)
CLIENT#2< REGISTER Rick pain 12-10-1951
CLIENT#2> ACK 1
CLIENT#1< LOGOUT
CLIENT#1> ACK 3
(client 1 closes)
CLIENT#2< LOGOUT
CLIENT#2> ERROR 3
(client 2 did not login)

4.2 Following and posting / PM
Server assumptions for example:
 Server currently has 3 registered users:
- “Morty” with password “a123”
- “Rick” with password “pain”
- “Bird-person” with password “Gubba”
 Followings:
- Morty follows Rick and Bird-person
- Rick follows Bird-person

CLIENT#1< LOGIN Morty a123 1
CLIENT#1> ACK 2
CLIENT#1< FOLLOW 0 Rick
CLIENT#1> ERROR 4
(Tried to follow users that he already follows, since both failed an error
returned)
CLIENT#2< LOGIN Bird-person Gubba 1
CLIENT#2> ACK 2
CLIENT#2< POST Gubba nub nub doo rah kah
CLIENT#2> ACK 5
CLIENT#1> NOTIFICATION Public Bird-person Gubba nub nub doo rah kah
(Morty follows bird-person and is online so he gets the message pushed)
CLIENT#3< LOGIN Rick pain 1
CLIENT#3> ACK 2
CLIENT#3> NOTIFICATION Public Bird-person Gubba nub nub doo rah kah
(Rick follows Bird-person, now that he logged-in he receives messages he
missed)
CLIENT#3< PM Bird-person why aren’t you following me?
CLIENT#3> ACK 6
CLIENT#2> NOTIFICATION PM Rick why aren’t you following me? 05-01-
2022
(Bird-person is online and was sent a PM, it is pushed right away to him)
CLIENT#3< POST wubba lubba dub dub @Bird-person is not following me
CLIENT#3> ACK 5
CLIENT#1> NOTIFICATION Public Rick wubba lubba dub dub @Bird-person
is not following me
CLIENT#2> NOTIFICATION Public Rick wubba lubba dub dub @Bird-person
is not following me
(Bird-person receives rick’s latest post because his @username appears in
it)
CLIENT#2< FOLLOW 0 Mortneey
CLIENT#2> ACK 4 1 Rick
(Bird-person failed to follow Morty because he misspelled his name)

4.3 STAT and unfollow
Server assumptions for example:
 Server currently has 3 registered users:
- “Morty” with password “a123”. Registered first
- “Rick” with password “pain”. Registered second
- “Bird-person” with password “Gubba”. Registered third
 Followings:
- Morty follows Rick and Bird-person
- Rick follows Bird-person
 Messages:
- Morty sent 2 posts and 3 PMs
- Rick sent 4 posts and 2 PMs
- Bird-person sent 1 post and 1 PM

CLIENT#1< LOGIN Morty a123 1
CLIENT#1> ACK 2
CLIENT#1< STAT Bird-person
CLIENT#1> ACK 8 47 1 2 0
CLIENT#1< POST @bird-person I will not follow you any more, you are not
social at all
CLIENT#1> ACK 5
(not one receives the post because bird person isn’t logged-in and no one
follows Morty, when bird person logs in he should get it)
CLIENT#1< FOLLOW 1 1 Bird-person
CLIENT#1> ACK 4 1 Bird-person
(Morty no longer follows bird-person and will not see his posts that do not
contain @Morty in them from now on)
CLIENT#1< STAT Bird-personaaaa
CLIENT#1> ERROR 8
(No such user Bird-personaaaa)
CLIENT#1< LOGOUT
CLIENT#1> ACK 3
(client 1 closes)
