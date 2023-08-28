package bgu.spl.net.api.bidi;



public abstract class Message {

    public abstract <T> Message process(BidiMessagingProtocolImpl<T> protocol);

    public abstract short getOpcode();

    public abstract String getOptionalString();

    public abstract Short[][] getOptionalShortArray();

}
