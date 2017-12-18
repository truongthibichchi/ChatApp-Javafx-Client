package connection;

import java.io.Serializable;

public enum MessageType implements Serializable{
    LOG_IN,
    SIGN_UP,
    CONNECTED,
    DISCONNECTED,
    CHAT,
    VOICE,
}
