package uk.co.roteala.common;

import com.fasterxml.jackson.databind.util.StdConverter;
import uk.co.roteala.common.monetary.Coin;

public class TransactionStatusConverter extends StdConverter<TransactionStatus, String> {
    @Override
    public String convert(TransactionStatus status) {
        return String.valueOf(status.getCode());
    }
}
