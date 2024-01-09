package uk.co.roteala.common.messenger;

import io.reactivex.rxjava3.functions.Consumer;
import io.vertx.core.buffer.Buffer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.publisher.Sinks;
import reactor.netty.Connection;
import uk.co.roteala.net.ConnectionsStorage;

import java.time.Duration;
import java.util.List;
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
        }
    }

    private void sendToServer(MessageTemplate template) {}

    private void sendToClient(MessageTemplate template) {
        List<String> serializedChunks = MessengerUtils.createChunks(template)
                .stream()
                .map(s -> s+MessengerUtils.delimiter)
                .collect(Collectors.toList());

        Flux.fromIterable(serializedChunks)
                .flatMap(chunk -> Mono.just(template.getOwner())
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
        log.info("Prepare to send: {}", template);

        List<String> serializedChunks = MessengerUtils.createChunks(template)
                .stream()
                .map(s -> (s+MessengerUtils.delimiter))
                .collect(Collectors.toList());

        Flux.fromIterable(serializedChunks)
                .flatMap(chunk -> Flux.fromIterable(this.connections.getClientConnections())
                        .doOnNext(netSocket -> netSocket.write(Buffer.buffer(chunk)))
                )
                .then()
                .subscribe();
    }
}
