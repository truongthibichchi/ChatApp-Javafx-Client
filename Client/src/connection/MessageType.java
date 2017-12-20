package connection;

import java.io.Serializable;

public enum MessageType implements Serializable{
    LOG_IN,
    CONNECTED,
    SIGN_UP,
    CHAT,
}
