package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.math.LongMath;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

@JsonDeserialize(using = CoinDeserializer.class)
public class Coin implements Monetary, Comparable<Coin>, Serializable {

    private final BigDecimal value;

    Coin(BigDecimal v) {
        this.value = v;
    }

    public static Coin valueOf(BigDecimal v) {
        if (v == null || v.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value must be non-null and non-negative");
        }
        return new Coin(v);
    }

    public Coin add(Coin other) {
        return new Coin(value.add(other.value));
    }

    public Coin subtract(Coin other) {
        return new Coin(value.subtract(other.value));
    }

    public static final Coin ZERO = Coin.valueOf(BigDecimal.ZERO);

    @Override
    public int compareTo(Coin other) {
        return value.compareTo(other.value);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    // Implement Monetary interface methods here

    // Implement other methods as needed
}
