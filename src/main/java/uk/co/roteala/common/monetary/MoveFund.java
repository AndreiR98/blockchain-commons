package uk.co.roteala.common.monetary;

public interface MoveFund {
    void execute(Fund fund);


    /**
     * During syncronization process nodes will call this method to reconstruct account balance from txs
     * */
    void reverseFunding(Fund fund);

    void executeRewardFund(Fund fund);
}
