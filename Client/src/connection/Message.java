package connection;

public class Message {
    private MessageType type;
    private Object content;

    public Message(){};
    public Message(MessageType type, Object content){
        this.type=type;
        this.content=content;
    }
    public MessageType getType(){return type;}
    public Object getContent(){return content;}

    public void setContent(Object content){
        this.content=content;
    }

    public void setType (MessageType type){
        this.type=type;
    }
}
