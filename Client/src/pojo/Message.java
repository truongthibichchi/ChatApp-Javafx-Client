package pojo;


import java.sql.Timestamp;


public class Message {
    private int id;
    private Integer conversationId;
    private String username;
    private String messagaeType;
    private String message;
    private Timestamp createAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getMessagaeType() {
        return messagaeType;
    }

    public void setMessagaeType(String messagaeType) {
        this.messagaeType = messagaeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }


}
