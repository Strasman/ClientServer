package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Message;

import java.util.LinkedList;

public class Ack extends Message {

    private short opcode;
    private short messageOpcode;
    private Object optional;


    public Ack(short messageOpcode, Object optional) {
        this.opcode = 10;
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    @Override
    public <T> Message process(BidiMessagingProtocolImpl<T> protocol) {
        return null;
    }

    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public String getOptionalString() {

        if(optional == null)
            return null;
        return optional.toString();
    }

    @Override
    public Short[][] getOptionalShortArray(){
        if(optional == null)
            return null;
        return (Short[][]) optional;
    }


}
