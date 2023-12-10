package uk.co.roteala.common.messenger;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import uk.co.roteala.common.BasicModel;

@Slf4j
@RequiredArgsConstructor
public class Messenger extends Flux<ByteBuf> {
    private final BasicModel model;
    private final EventTypes eventTypes;
    private final EventActions eventActions;
    @Override
    public void subscribe(CoreSubscriber<? super ByteBuf> coreSubscriber) {

        Flux.fromIterable(MessengerUtils.createChunks(model, eventTypes, eventActions))
                .doOnNext(coreSubscriber::onNext)
                .doOnNext(byteBuf -> {
                    log.info("Sending:{}", byteBuf);
                })
                .doOnComplete(coreSubscriber::onComplete)
                .subscribe();
    }
}
