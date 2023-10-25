package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.math.LongMath;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;

@JsonDeserialize(using = CoinDeserializer.class)
public class Coin implements Comparable<Coin>, Serializable {
    private final BigInteger value;
    private Coin(BigInteger value) {
        this.value = value;
    }

    public static Coin of(BigInteger value) {
        if (value == null || value.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be non-null and non-negative");
        }
        return new Coin(value);
    }

    public static Coin of(String value) {
        if(value == null || new BigInteger(value, 10).compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be non-null and non-negative");
        }

        return new Coin(new BigInteger(value, 10));
    }

    public Coin add(Coin other) {
        return new Coin(this.value.add(other.value));
    }

    public Coin subtract(Coin other) {
        return new Coin(this.value.subtract(other.value));
    }

    public String getStringValue() {
        return this.value.toString(16);
    }

    public BigInteger getValue() {
        return this.value;
    }

    @Override
    public int compareTo(Coin other) {
        return value.compareTo(other.value);
    }
}
