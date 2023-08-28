package bgu.spl.net.api.bidi.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Message;

import java.util.LinkedList;

public class Error extends Message {

    private final short opcode;
    private short messageOpcode;

    public Error(short messageOpcode) {
        this.opcode = 11;
        this.messageOpcode = messageOpcode;
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
        return null;
    }

    @Override
    public Short[][] getOptionalShortArray() {
        return null;
    }


}
