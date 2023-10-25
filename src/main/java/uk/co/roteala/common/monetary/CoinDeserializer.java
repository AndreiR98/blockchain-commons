package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class CoinDeserializer extends JsonDeserializer<Coin> {

    @Override
    public Coin deserialize(JsonParser jsonParser, DeserializationContext deserializationContext){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonParser.getText(), new TypeReference<Coin>() {});
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.DESERIALIZATION_FAILED);
        }
    }
}
