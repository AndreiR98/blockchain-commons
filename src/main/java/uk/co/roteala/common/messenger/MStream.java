package uk.co.roteala.common.messenger;

import reactor.netty.NettyInbound;

public interface MStream<V> {
    <V> MStream<V> source(final SourceSupplier<? super V> sourceSupplier);
    <V> MStream<V> assembler(final AssemblerSupplier<? super V> assemblerSupplier);

    MStream<V>[] branch(final Predicate<? super V>... predicates);
    void executor(final ExecutorSupplier<? super V> executorSupplier);
}
