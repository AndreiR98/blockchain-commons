package uk.co.roteala.net;

import java.net.InetAddress;
import org.springframework.lang.Nullable;

public interface StreamConnectionFactory {
    @Nullable
    StreamConnection getNewConnection(InetAddress inetAddress, int port);
}
