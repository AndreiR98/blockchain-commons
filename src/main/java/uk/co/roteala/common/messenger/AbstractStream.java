package uk.co.roteala.common.messenger;

public interface AbstractStream<I, O> {
    I inboundSupplier(final InboundSupplier<? super I> inboundSupplier);
    O outboundSupplier(final OutboundSupplier<? super O> outboundSupplier);
}
