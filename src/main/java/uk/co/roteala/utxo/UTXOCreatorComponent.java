package uk.co.roteala.utxo;

public interface UTXOCreatorComponent {
    /**
     * Creates UTXO for sender
     * */
    void createUTXO();

    /**
     * Return the UTXO
     * */
    void getUTXOComponent();

    /**
     * set utxo
     * */
    void setUTXOComponent();

    void getUTXOin();

    void getUTXOout();
}
