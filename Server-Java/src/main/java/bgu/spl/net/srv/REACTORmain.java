package bgu.spl.net.impl.system;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.Server;

public class REACTORmain {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        int threadNum = 2;
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
        try {
            port = Integer.decode(args[0]);
            threadNum = Integer.decode(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        Server.reactor(threadNum, port, BidiMessagingProtocolImpl::new, MessageEncoderDecoderImpl::new).serve();

    }
}
