package connection;

import java.net.Socket;

public interface ConnectionCallback{
    void onConnectToServerSucceeded(Socket socketToServer);
    void onConnectToServerFailed();
}
