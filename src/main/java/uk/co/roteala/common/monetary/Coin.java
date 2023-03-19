package uk.co.roteala.common.monetary;

import com.google.common.math.LongMath;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

public class Coin implements Monetary, Comparable<Coin>, Serializable {

    public final long value;

    private Coin(final long v) {
        this.value = v;
    }

    public static Coin valueOf(final long l) {
        return new Coin(l);
    }

    public static final Coin ZERO = Coin.valueOf(0);
    @Override
    public int compareTo(@NotNull Coin o) {
        return 0;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public int sigNum() {
        return 0;
    }
}
