package uk.co.roteala.common.messenger;

import reactor.netty.NettyInbound;

public interface MStream<V> {
    <V> MStream<V> reader(final ReaderSupplier<? super V>... readerSupplier);
    <V> MStream<V> assembler(final AssemblerSupplier<? super V> assemblerSupplier);

    MStream<V>[] split(final Predicate<? super V>... predicates);
    void executor(final ExecutorSupplier<? super V> executorSupplier);
}
