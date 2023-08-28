package bgu.spl.net.impl.system;


import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Server;

public class TPCmain {
    public static void main(String[] args) {
        String port1 = args[1];
        if (args.length < 1) throw new IllegalArgumentException();
            Integer port = Integer.parseInt(port1);

        Server<Message> tpc = Server.threadPerClient(port, BidiMessagingProtocolImpl::new, MessageEncoderDecoderImpl::new);
        tpc.serve();
    }
}
