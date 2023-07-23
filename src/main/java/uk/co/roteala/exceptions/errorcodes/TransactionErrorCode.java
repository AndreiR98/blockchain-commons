package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum TransactionErrorCode implements ErrorCode {
    TRANSACTION_IDENTITY("transaction.001"),
    AMOUNT_GREATER_ACCOUNT("transaction.002"),
    TRANSACTION_NOT_FOUND("transaction.003"),
    TRANSACTION_FAILED("transaction.004");

    TransactionErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
