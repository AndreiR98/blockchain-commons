package uk.co.roteala.common.messenger;

import reactor.netty.NettyInbound;

public interface SourceReader<I, R> {
    <R> SourceReader<I, R> source(I inbound);

    void setReader(final ReaderSupplier<? super I>... readerSupplier);
}
