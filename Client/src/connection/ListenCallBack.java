package connection;

import java.net.Socket;

public interface ListenCallBack {
    void onConnectionReceived (Socket socketToClient);
}
