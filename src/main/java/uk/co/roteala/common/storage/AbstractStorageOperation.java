package uk.co.roteala.common.storage;

import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import uk.co.roteala.common.BasicModel;

import java.util.List;

public abstract class AbstractStorageOperation {

    protected Operator operator;
    protected Object value;
    protected SearchField searchField;//TimeStamp, Nonce, value, hash......
    protected Integer pageSize;

    protected boolean isReversed;

    protected ColumnFamilyTypes handler;

    static int DEFAULT_PAGE_SIZE = 100;
    static int DEFAULT_TRANSACTIONS_SIZE_PER_BLOCK = 1024 * 1024;//1MB

    public AbstractStorageOperation withCriteria(Operator operator, Object value, SearchField searchField) {
        this.searchField = searchField;
        this.operator = operator;
        this.value = value;
        return this;
    }

    public AbstractStorageOperation withHandler(ColumnFamilyTypes handler) {
        this.handler = handler;
        return this;
    }

    public AbstractStorageOperation reversed(boolean reversed) {
        this.isReversed = reversed;
        return this;
    }

    public abstract List<BasicModel> asBasicModel();

    public abstract List<String> asKey();

    public AbstractStorageOperation page(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
