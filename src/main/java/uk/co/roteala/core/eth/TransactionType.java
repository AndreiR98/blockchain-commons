package uk.co.roteala.core.eth;

public enum TransactionType {
    LEGACY(null),
    EIP2930(((byte) 0x01)),
    EIP1559(((byte) 0x02));

    Byte type;

    TransactionType(final Byte type) {
        this.type = type;
    }

    public Byte getRlpType() {
        return type;
    }

    public boolean isLegacy() {
        return this.equals(TransactionType.LEGACY);
    }

    public boolean isEip1559() {
        return this.equals(TransactionType.EIP1559);
    }

    public boolean isEip2930() {
        return this.equals(TransactionType.EIP2930);
    }
}
