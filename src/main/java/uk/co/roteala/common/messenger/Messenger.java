package uk.co.roteala.common.messenger;

import io.reactivex.rxjava3.functions.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.Sinks;
import reactor.netty.Connection;
import uk.co.roteala.net.ConnectionsStorage;

import java.time.Duration;


@Slf4j
@Component
@RequiredArgsConstructor
public class Messenger implements Consumer<Flux<MessageTemplate>> {
    private final ConnectionsStorage connections;
    private final Sinks.Many<MessageTemplate> sink;
    /**
     * Send a message to a receiving group
     * */
    public void send(MessageTemplate template, ReceivingGroup group) {
        template.setGroup(group);
        sink.tryEmitNext(template);
    }

    @Override
    public void accept(Flux<MessageTemplate> messageTemplateFlux) {
        messageTemplateFlux.doOnEach(this::processor)
                .then()
                .subscribe();
    }

    private void processor(Signal<MessageTemplate> templateSignal) {
        final MessageTemplate template = templateSignal.get();
        log.info("Processing:{}", template);

        switch (template.getGroup()) {
            case BROKER:
                sendToBroker(template);
            case SERVERS:
                sendToServers(template);
            case PEERS:
                sendToPeers(template);
            case CLIENTS:
                sendToClients(template);
        }
    }

    private void sendToBroker(MessageTemplate template) {
        this.connections.getBrokerConnection()
                .outbound()
                .sendObject(Flux.fromStream(MessengerUtils.createChunks(template).parallelStream())
                        .delayElements(Duration.ofMillis(120)))
                .then()
                .subscribe();
    }

    private void sendToServers(MessageTemplate template) {}

    private void sendToPeers(MessageTemplate template) {}

    private void sendToClients(MessageTemplate template) {
        for(Connection connection : this.connections.getClientConnections()) {
            connection.outbound()
                    .sendObject(Flux.fromStream(MessengerUtils.createChunks(template).parallelStream())
                            .delayElements(Duration.ofMillis(120)))
                    .then()
                    .subscribe();
        }
    }
}
