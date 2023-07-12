package uk.co.roteala.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.co.roteala.common.monetary.Coin;

import java.io.IOException;
import java.math.BigDecimal;

public class TransactionStatusDeserializer extends JsonDeserializer<TransactionStatus> {

    @Override
    public TransactionStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return TransactionStatus.valueOfCode(jsonParser.getText());
    }
}
