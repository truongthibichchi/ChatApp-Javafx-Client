package connection;

import java.io.Serializable;

public enum MessageType implements Serializable{
    LOG_IN,
    CONNECTED,
    LOG_IN_SUCEEDED,
    SIGN_UP,
    CHAT,
}
