package uk.co.roteala.common.messenger;

import io.reactivex.rxjava3.functions.Consumer;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.scheduler.Schedulers;
import uk.co.roteala.net.ConnectionsStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class Messenger implements Consumer<Flux<MessageTemplate>> {
    private final ConnectionsStorage connections;

    @Override
    public void accept(Flux<MessageTemplate> messageTemplateFlux) {
        messageTemplateFlux.doOnEach(this::processor)
                .then()
                .subscribe();
    }

    private void processor(Signal<MessageTemplate> templateSignal) {
        final MessageTemplate template = templateSignal.get();

        log.info("Event: {} Action: {} to: {}",template.getEventType(), template.getEventAction(), template.getGroup());

        switch (template.getGroup()) {
            case BROKER:
                sendToBroker(template);
                break;
            case SERVERS:
                sendToServers(template);
                break;
            case PEERS:
                sendToPeers(template);
                break;
            case CLIENTS:
                sendToClients(template);
                break;
            case CLIENT:
                sendToClient(template);
                break;
            case SERVER:
                sendToServer(template);
                break;
            case ALL:
                sendToAll(template);
                break;
        }
    }

    private void sendToAll(MessageTemplate template) {
        List<NetSocket> asServerConnections = this.connections.getAsServerConnections();
        List<NetSocket> asClientConnections = this.connections.getAsClientConnections();

        if (template.getWithOut() != null && !template.getWithOut().isEmpty()) {
            asServerConnections = this.connections.getAsServerConnections()
                    .stream().filter(netSocket -> !template.getWithOut().contains(netSocket))
                    .collect(Collectors.toList());

            asClientConnections = this.connections.getAsClientConnections()
                    .stream().filter(netSocket -> !template.getWithOut().contains(netSocket))
                    .collect(Collectors.toList());
        }

        Flux<String> serializedChunks = Flux.fromIterable(MessengerUtils.createChunks(template))
                .map(s -> s + MessengerUtils.delimiter);

        Flux<NetSocket> connections = Flux.fromIterable(asServerConnections)
                        .mergeWith(Flux.fromIterable(asClientConnections))
                                .mergeWith(Mono.justOrEmpty(this.connections.getBrokerConnection()));

        connections.parallel()
                .runOn(Schedulers.parallel())
                .flatMap(netSocket -> serializedChunks.doOnNext(chunk -> netSocket.write(Buffer.buffer(chunk))))
                .then().subscribe();
    }

    private void sendToServer(MessageTemplate template) {}

    private void sendToClient(MessageTemplate template) {
        List<String> serializedChunks = MessengerUtils.createChunks(template)
                .stream()
                .map(s -> s+MessengerUtils.delimiter)
                .collect(Collectors.toList());

        Flux.fromIterable(serializedChunks)
                .flatMap(chunk -> Mono.justOrEmpty(template.getOwner())
                        .doOnNext(netSocket -> netSocket.write(Buffer.buffer(chunk)))
                ).then()
                .subscribe();
    }

    private void sendToBroker(MessageTemplate template) {
        List<String> serializedChunks = MessengerUtils.createChunks(template)
                .stream()
                .map(s -> s+MessengerUtils.delimiter)
                .collect(Collectors.toList());

        Flux.fromIterable(serializedChunks)
                .flatMap(chunk -> Mono.just(this.connections.getBrokerConnection())
                        .doOnNext(netSocket -> netSocket.write(Buffer.buffer(chunk)))
                ).then()
                .subscribe();
    }

    private void sendToServers(MessageTemplate template) {}

    private void sendToPeers(MessageTemplate template) {}

    private void sendToClients(MessageTemplate template) {
        List<String> serializedChunks = MessengerUtils.createChunks(template)
                .stream()
                .map(s -> (s+MessengerUtils.delimiter))
                .collect(Collectors.toList());

        Flux.fromIterable(serializedChunks)
                .flatMap(chunk -> Flux.fromIterable(this.connections.getAsServerConnections())
                        .doOnNext(netSocket -> netSocket.write(Buffer.buffer(chunk)))
                )
                .then()
                .subscribe();
    }
}
