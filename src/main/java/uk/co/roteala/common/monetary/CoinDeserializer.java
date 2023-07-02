package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class CoinDeserializer extends JsonDeserializer<Coin> {

    @Override
    public Coin deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return new Coin(new BigDecimal(jsonParser.getText()));
    }
}
