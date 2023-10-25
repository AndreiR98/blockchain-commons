package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;

import java.util.List;

public class CoinConverter extends StdConverter<Coin, String> {
    @Override
    public String convert(Coin coin) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.writeValueAsString(coin);
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
        }
    }
}
