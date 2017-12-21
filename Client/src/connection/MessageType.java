package connection;

import java.io.Serializable;

public enum MessageType implements Serializable{
    LOG_IN,
    LOG_IN_SUCCEEDED,
    USER_EXISTED,
    SIGN_UP,
    SIGN_UP_CUCCEEDED,
    CHAT,
}
