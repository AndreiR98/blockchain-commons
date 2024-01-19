package uk.co.roteala.common.monetary;

public interface MoveFund {
    void execute(Funding funding);


    /**
     * During syncronization process nodes will call this method to reconstruct account balance from txs
     * */
    void reverseFunding(Funding funding);

    void executeRewardFund(Funding funding);
}
