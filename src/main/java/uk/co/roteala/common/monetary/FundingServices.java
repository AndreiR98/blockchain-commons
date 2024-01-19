package uk.co.roteala.common.monetary;

import io.reactivex.rxjava3.functions.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import uk.co.roteala.common.Account;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.common.MempoolTransaction;
import uk.co.roteala.common.Transaction;
import uk.co.roteala.common.storage.ColumnFamilyTypes;
import uk.co.roteala.common.storage.Storage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
//@Service
@RequiredArgsConstructor
public class FundingServices implements Consumer<Flux<Funding>> {
    private final Storage stateTrieStorage;

//    public FundingServices(Storage stateTrieStorage) {
//        this.stateTrieStorage = stateTrieStorage;
//    }

    /***/
    public void processFunding(Funding funding) {
        if (funding.getType() == Funding.TransactionType.TRANSACTION) {
            processTransactionBalance(funding);
        } else if (funding.getType() == Funding.TransactionType.MEMPOOL) {
            processMempoolBalances(funding);
        }
    }

    private void processTransactionBalance(Funding funding) {
        final BigInteger amount = funding.getAmount();
        final BigInteger networkFees = funding.getNetworkFees();
        final BigInteger processingFees = funding.getProcessingFees();

        //Calculate how much the sender has to pay;
        final BigInteger senderPayAmount = amount
                .add(networkFees)
                .add(processingFees);

        /**
         * This will move first the amount from virtual balance from sender to receiver then it will move the fees to the miner
         * */
        Account sender = Optional.ofNullable((Account) this.stateTrieStorage.get(ColumnFamilyTypes.ACCOUNTS,
                        funding.getSourceAccountAddress().toLowerCase()
                                .getBytes(StandardCharsets.UTF_8)))
                .orElseGet(() -> Account.empty(funding.getSourceAccountAddress().toLowerCase()));

        sender.updateVirtualBalance(senderPayAmount, VirtualBalanceSign.PLUS);
        sender.setBalance(sender.getBalance().subtract(senderPayAmount));

        this.stateTrieStorage.modify(ColumnFamilyTypes.ACCOUNTS, sender.getAddress()
                .getBytes(StandardCharsets.UTF_8), sender);

        Account receiver = Optional.ofNullable((Account) this.stateTrieStorage.get(ColumnFamilyTypes.ACCOUNTS,
                        funding.getTargetAccountAddress().toLowerCase()
                                .getBytes(StandardCharsets.UTF_8)))
                .orElseGet(() -> Account.empty(funding.getTargetAccountAddress().toLowerCase()));

        receiver.updateVirtualBalance(amount, VirtualBalanceSign.MINUS);
        receiver.setBalance(receiver.getBalance().add(amount));

        this.stateTrieStorage.modify(ColumnFamilyTypes.ACCOUNTS, receiver.getAddress()
                .getBytes(StandardCharsets.UTF_8), receiver);

        Account miner = Optional.ofNullable((Account) this.stateTrieStorage.get(ColumnFamilyTypes.ACCOUNTS,
                        funding.getMinerAddress().toLowerCase()
                                .getBytes(StandardCharsets.UTF_8)))
                .orElseGet(() -> Account.empty(funding.getMinerAddress().toLowerCase()));

        miner.setBalance(miner.getBalance()
                .add(processingFees
                        .add(networkFees)));

        this.stateTrieStorage.modify(ColumnFamilyTypes.ACCOUNTS, miner.getAddress()
                .getBytes(StandardCharsets.UTF_8), miner);

    }

    /**
     * This will update the accounts virtual balance and virtual balance sign
     * It will add ut to the total sum: virtualBalance = spending + receiving
     * */
    private void processMempoolBalances(Funding funding) {
        //if (mempoolTransaction.isValid()) {
            final BigInteger amount = funding.getAmount();
            final BigInteger networkFees = funding.getNetworkFees();
            final BigInteger processingFees = funding.getProcessingFees();

            //Calculate how much the sender has to pay;
            final BigInteger senderPayAmount = amount
                    .add(networkFees)
                    .add(processingFees);

            Account sender = Optional.ofNullable((Account) this.stateTrieStorage.get(ColumnFamilyTypes.ACCOUNTS,
                    funding.getSourceAccountAddress().toLowerCase()
                    .getBytes(StandardCharsets.UTF_8)))
                    .orElseGet(() -> Account.empty(funding.getSourceAccountAddress().toLowerCase()));
            sender.updateVirtualBalance(senderPayAmount, VirtualBalanceSign.MINUS);

            List<String> transactionsOut = sender.getTransactionsOut();
            transactionsOut.add(funding.getHash());

            sender.setTransactionsOut(transactionsOut);

            this.stateTrieStorage.modify(ColumnFamilyTypes.ACCOUNTS, sender.getAddress()
                    .getBytes(StandardCharsets.UTF_8), sender);

            Account receiver = Optional.ofNullable((Account) this.stateTrieStorage.get(ColumnFamilyTypes.ACCOUNTS,
                        funding.getTargetAccountAddress().toLowerCase()
                                .getBytes(StandardCharsets.UTF_8)))
                .orElseGet(() -> Account.empty(funding.getTargetAccountAddress().toLowerCase()));

            receiver.updateVirtualBalance(amount, VirtualBalanceSign.PLUS);

            List<String> transactionsIn = receiver.getTransactionsIn();
            transactionsIn.add(funding.getHash());

            receiver.setTransactionsIn(transactionsIn);

        this.stateTrieStorage.modify(ColumnFamilyTypes.ACCOUNTS, receiver.getAddress()
                .getBytes(StandardCharsets.UTF_8), receiver);
        //}
    }

    @Override
    public void accept(Flux<Funding> fundingFlux) {
        fundingFlux.doOnNext(this::processFunding)
                .delayElements(Duration.ofMillis(10))
                .parallel()
                .runOn(Schedulers.parallel())
                .then()
                .subscribe();
    }
}
