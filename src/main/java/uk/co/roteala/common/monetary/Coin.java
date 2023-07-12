package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.math.LongMath;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

@JsonDeserialize(using = CoinDeserializer.class)
public class Coin implements Monetary, Comparable<Coin>, Serializable {

    public final BigDecimal value;

    //public final CoinSign sign;

    Coin(final BigDecimal v) {
        this.value = v;
    }

    public static Coin valueOf(final BigDecimal l) {
        return new Coin(l);
    }

    public Coin add(final Coin value) {
        //final BigDecimal finalValue = this.value.add(value.value);
        return new Coin(this.value.add(value.value));
    }

    public Coin min(final Coin value) {
        return new Coin(this.value.min(value.value));
    }

    public Coin plus(final Coin value) {
        return add(value);
    }

    public Coin minus(final Coin value) {
        return min(value);
    }

    public static final Coin ZERO = Coin.valueOf(BigDecimal.ZERO);
    @Override
    public int compareTo(@NotNull Coin o) {
        return 0;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public int sigNum() {
        return 0;
    }
}
