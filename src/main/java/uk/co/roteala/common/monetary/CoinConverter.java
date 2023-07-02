package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.databind.util.StdConverter;

public class CoinConverter extends StdConverter<Coin, String> {
    @Override
    public String convert(Coin coin) {
        return String.valueOf(coin.getValue());
    }
}
