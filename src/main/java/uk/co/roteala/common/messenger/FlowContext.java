package uk.co.roteala.common.messenger;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlowContext implements StreamContext {
    private List<SourceSupplier> sourceSuppliers;
    private Cache<MessageKey, MessageAssembled> cache = Caffeine.newBuilder()
            .maximumSize(500)
            .build();
    @Override
    public List<SourceSupplier> sourceSuppliers() {
        return this.sourceSuppliers;
    }
}
