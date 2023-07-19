package uk.co.roteala.utils;

import lombok.experimental.UtilityClass;
import uk.co.roteala.common.Block;
import uk.co.roteala.common.PseudoTransaction;
import uk.co.roteala.common.Transaction;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.security.utils.HashingService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BlockchainUtils {
    public String mapHashed(PseudoTransaction pseudoTransaction, Block block, int index) {
        Transaction transaction = new Transaction();
        transaction.setPseudoHash(pseudoTransaction.getPseudoHash());
        transaction.setBlockNumber(block.getIndex());
        transaction.setFrom(pseudoTransaction.getFrom());
        transaction.setTo(pseudoTransaction.getTo());
        transaction.setFees(null);
        transaction.setVersion(pseudoTransaction.getVersion());
        transaction.setTransactionIndex(index);
        transaction.setValue(pseudoTransaction.getValue());
        transaction.setNonce(pseudoTransaction.getNonce());
        transaction.setTimeStamp(pseudoTransaction.getTimeStamp());
        transaction.setBlockTime(block.getTimeStamp());
        transaction.setPubKeyHash(pseudoTransaction.getPubKeyHash());
        transaction.setStatus(pseudoTransaction.getStatus());
        transaction.setSignature(pseudoTransaction.getSignature());
        transaction.setHash(transaction.computeHash());

        StringBuilder bothHashes = new StringBuilder();
        bothHashes.append(pseudoTransaction.getPseudoHash());
        bothHashes.append("_");
        bothHashes.append(transaction.getHash());

        return bothHashes.toString();
    }

    public String markleRootGenerator(List<String> transactionHashes) {
        if(transactionHashes.isEmpty()) {
            return null;
        }

        List<String> layer = transactionHashes;

        while(layer.size() > 1) {
            List<String> nextLayer = new ArrayList<>();

            for(int i = 0; i < layer.size(); i += 2) {
                String left = layer.get(i);
                String right = (i + 1 < layer.size()) ? layer.get(i + 1) : left;
                String combinedHash = HashingService
                        .bytesToHexString(HashingService
                                .sha256Hash((left + right).getBytes()));

                nextLayer.add(combinedHash);
            }

            layer = nextLayer;
        }

        return  layer.get(0);
    }

    public List<List<String>> splitPseudoHashMatches(List<String> pseudoHashMatches) {
        if(pseudoHashMatches.isEmpty()) {
            return null;
        }

        List<List<String>> separatedHashes = new ArrayList<>();

        List<String> transactionHash = new ArrayList<>();
        List<String> pseudoHash = new ArrayList<>();

        pseudoHashMatches.forEach(hash -> {
            String[] splitHash = hash.split("_");

            transactionHash.add(splitHash[1]);
            pseudoHash.add(splitHash[0]);
        });

        separatedHashes.add(pseudoHash);
        separatedHashes.add(transactionHash);

        return separatedHashes;
    }

    public boolean computedTargetValue(String proposedHash, Integer coefficient) {
        int currentCount = 0;

        for(int i = 0; i < proposedHash.length(); i++) {
            if(proposedHash.charAt(i) == '0') {
                currentCount++;
            } else {
                break;
            }
        }

        return currentCount >= coefficient;
    }
}
