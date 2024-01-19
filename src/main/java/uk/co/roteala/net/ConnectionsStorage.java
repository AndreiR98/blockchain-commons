package uk.co.roteala.net;

import io.vertx.core.net.NetSocket;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ConnectionsStorage {
    /**
     * Represents a set of connections initiated by the blockchain node acting as a client in a peer-to-peer network.
     * These connections are established by your node to communicate with other nodes in the blockchain network.
     * Typically used for sending transactions, requesting blocks, and general peer-to-peer communication.
     */
    private List<NetSocket> asClientConnections = new ArrayList<>();

    /**
     * Represents a set of connections accepted by the blockchain node acting as a server in a peer-to-peer network.
     * Nodes from the blockchain network establish connections to your node. Your node acts as a server,
     * receiving transactions, blocks, and other network-related information from connected nodes.
     */
    private List<NetSocket> asServerConnections = new ArrayList<>();

    /**
     * Represents a single connection to a broker. This connection may serve a specific purpose,
     * such as interacting with a centralized service or coordinating activities between nodes.
     * The broker connection might be used for tasks beyond regular peer-to-peer communication in the blockchain network.
     */
    private NetSocket brokerConnection;
}
