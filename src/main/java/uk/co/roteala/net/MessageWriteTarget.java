package uk.co.roteala.net;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;

public interface MessageWriteTarget {
    /**
     * Writes the given bytes to the remote server. The returned future will complete when all bytes
     * have been written to the OS network buffer.
     */
    ListenableFuture writeBytes(byte[] message) throws IOException;
    /**
     * Closes the connection to the server, triggering the {@link StreamConnection#connectionClosed()}
     * event on the network-handling thread where all callbacks occur.
     */
    void closeConnection();
}
