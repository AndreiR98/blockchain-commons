package uk.co.roteala.exceptions.errorcodes;

import uk.co.roteala.exceptions.ErrorCode;

public enum AccountErrorCode implements ErrorCode {
    ACCOUNT_NOT_FOUND("account.001"),
    ADDRESS_INVALID("account.002"),
    ACCOUNT_FAILED("account.003");
    AccountErrorCode(String key) {
        this.key = key;
    }

    final String key;

    @Override
    public String getKey() {
        return key;
    }
}
