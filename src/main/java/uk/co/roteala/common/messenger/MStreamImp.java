package uk.co.roteala.common.messenger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.NettyInbound;

@Slf4j
@Configuration
public class MStreamImp<V> implements MStream<V> {
    private final StreamContext context;

    public MStreamImp() {
        this.context = new FlowContext();
    }

    @Override
    public <V> MStream<V> source(SourceSupplier<? super V> sourceSupplier) {
        this.context.sourceSuppliers().add(sourceSupplier);
        return null;
    }

    @Override
    public <V> MStream<V> assembler(AssemblerSupplier<? super V> assemblerSupplier) {
        return null;
    }

    @Override
    public MStream<V>[] branch(Predicate<? super V>... predicates) {
        return new MStream[0];
    }

    @Override
    public void executor(ExecutorSupplier<? super V> executorSupplier) {
    }
}
