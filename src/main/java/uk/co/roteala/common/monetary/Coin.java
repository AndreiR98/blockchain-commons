package uk.co.roteala.common.monetary;

import com.google.common.math.LongMath;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

public class Coin implements Monetary, Comparable<Coin>, Serializable {
    @Override
    public int compareTo(@NotNull Coin o) {
        return 0;
    }

    @Override
    public BigDecimal getValue() {
        return null;
    }

    @Override
    public int sigNum() {
        return 0;
    }
}
