package uk.co.roteala.utils;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.common.*;
import uk.co.roteala.common.storage.ColumnFamilyTypes;
import uk.co.roteala.common.storage.Storage;
import uk.co.roteala.exceptions.MiningException;
import uk.co.roteala.exceptions.errorcodes.MiningErrorCode;
import uk.co.roteala.security.utils.CryptographyUtils;
import uk.co.roteala.security.utils.HashingService;


import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@UtilityClass
public class BlockchainUtils {

    public boolean isValidBlock(Block newMinedBlock, int target, Storage storage) {
        // Verify if the hash match
        if (!newMinedBlock.getHash().equals(newMinedBlock.computeHash())) {
            throw new MiningException(MiningErrorCode.HASH_MATCH);
        }

        // Check if the markle root is correct
        if (!newMinedBlock.getTransactions().isEmpty() &&
                !Objects.equals(newMinedBlock.getHeader().getMarkleRoot(),
                        markleRootGenerator(newMinedBlock.getTransactions()))) {
            throw new MiningException(MiningErrorCode.MARKLEROOT);
        }

        if (newMinedBlock.getTransactions().isEmpty() &&
                !Objects.equals(newMinedBlock.getHeader().getMarkleRoot(), Constants.DEFAULT_HASH)) {
            throw new MiningException(MiningErrorCode.MARKLEROOT);
        }

        //Check previous hash

        // Check if the hash meets the target difficulty
        if (!computedTargetValue(newMinedBlock.getHash(), target)) {
            throw new MiningException(MiningErrorCode.TARGET);
        }

        // Verify each transaction hash
        for (String mempoolTransactionHash : newMinedBlock.getTransactions()) {
            if (!validTransactionHash(mempoolTransactionHash) &&
                    !storage.has(ColumnFamilyTypes.TRANSACTIONS, mempoolTransactionHash.getBytes(StandardCharsets.UTF_8))) {
                throw new MiningException(MiningErrorCode.TRANSACTION_NOT_FOUND);
            }
        }

        return true;
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
        if (hash == null || hash.isEmpty()) {
            return false;
        }

        return hash.startsWith("0x") && hash.length() == 66;
    }


    public boolean validBlockHash(String hash) {
        if(hash == null || hash.isEmpty()) {
            return false;
        }

        return hash.length() == 64;
    }

    public boolean validAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        if (address.length() != 40 && address.length() != 42) {
            return false;
        }

        if (!address.matches("^(0x)?[0-9a-fA-F]+$")) {
            return false;
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

    public String formatIPAddress(SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
            return inetSocketAddress.getAddress().getHostAddress();
        }

        return null;
    }

    public String convertToETHHex(int number) {
        return "0x"+Integer.toHexString(number);
    }

    public String convertToETHHex(BigInteger bigInteger) {
        return "0x"+bigInteger.toString(16);
    }


}
