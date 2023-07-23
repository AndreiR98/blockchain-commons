package uk.co.roteala.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.common.Block;
import uk.co.roteala.common.PseudoTransaction;
import uk.co.roteala.common.Transaction;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.security.utils.HashingService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
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

    /**
     * TODO::Recreate the markle root based on the orde in the block
     * */
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

    public boolean validTransactionHash(String hash) {
        if(hash == null || hash.isEmpty()) {
            return false;
        }

        return hash.length() == 96;
    }

    public boolean validBlockAddress(String hash) {
        if(hash == null || hash.isEmpty()) {
            return false;
        }

        return hash.length() == 64;
    }

    public boolean validAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        // Check address length (between 26 and 35 characters, excluding the version prefix)
        if (address.length() < 26 || address.length() > 35) {
            return false;
        }

        // Check for invalid characters
        for (char c : address.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isLowerCase(c) && !Character.isUpperCase(c)) {
                return false;
            }

        }

        // Check address prefix (starts with '1', '3', or 'bc1')
        if (!address.matches("^(1|[13]|bc1)[a-zA-Z0-9]+$")) {
            return false;
        }

        // Validate the checksum
        byte[] decodedAddress = Base58.decode(address);
        if (decodedAddress.length != 25) {
            return false;
        }

        byte[] decodedWithoutCheckSum = Arrays.copyOfRange(decodedAddress, 0, 21);



        // Double SHA256 hash of the address (excluding the checksum part)
        byte[] sha256Hash1 = HashingService.doubleSHA256(decodedWithoutCheckSum);

        // Compare the first 4 bytes of the checksum
        for (int i = 0; i < 4; i++) {
            if (sha256Hash1[i] != decodedAddress[21 + i]) {
                return false;
            }

        }

        return true;
    }

    public boolean isInteger(String integer){
        try {
            Integer.parseInt(integer);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
