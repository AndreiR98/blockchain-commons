package uk.co.roteala.net;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Service;

import java.net.SocketAddress;

public interface ClientConnectionManager extends Service {
    ListenableFuture<SocketAddress> openConnection(SocketAddress serverAddress, StreamConnection connection);

    /** Gets the number of connected peers */
    int getConnectedClientCount();

    /** Closes n peer connections */
    void closeConnections(int n);
}
