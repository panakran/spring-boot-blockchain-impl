package com.pkran.blockchain.Utilities;

import com.pkran.blockchain.models.*;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public final class BlockchainUtil {

    //Calculate new hash based on blocks contents
    public static String calculateHash(Block block) {
        return SecurityUtil.applySha256(
                block.getPreviousHash() +
                        block.getTimeStamp() +
                        block.getNonce() +
                        block.getMerkleRoot()
        );
    }

    // This Calculates the transaction hash (which will be used as its Id)
    public static String calculateHash(Transaction transaction) {
        return SecurityUtil.applySha256(
                StringUtil.getStringFromKey(transaction.getSender()) +
                        StringUtil.getStringFromKey(transaction.getRecipient()) +
                        transaction.getValue() + UUID.randomUUID().toString()
        );
    }

    public static void mineBlock(Integer difficulty, Block block) {
        block.setMerkleRoot(BlockchainUtil.getMerkleRoot(block.getTransactions()));
        String target = StringUtil.createZerosStringFromInteger(difficulty);
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(BlockchainUtil.calculateHash(block));
        }
        System.out.println("Block Mined!!! : " + block.getHash());

    }

    //Signs all the data we dont wish to be tampered with.
    public static void generateSignature(PrivateKey privateKey, Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + transaction.getValue();
        transaction.setSignature(SecurityUtil.applyECDSASig(privateKey, data));
    }

    //Verifies the data we signed hasnt been tampered with
    public static boolean verifySignature(Transaction transaction) {
        String data = StringUtil.getStringFromKey(transaction.getSender()) + StringUtil.getStringFromKey(transaction.getRecipient()) + transaction.getValue();
        return SecurityUtil.verifyECDSASig(transaction.getSender(), data, transaction.getSignature());
    }


    //returns sum of inputs(UTXOs) values
    public static Float getInputsValue(Transaction transaction) {
        return transaction.getInputs()
                .stream()
                .map(TransactionInput::getUTXO)
                .filter(Objects::nonNull)
                .map(TransactionOutput::getValue)
                .reduce((float) 0, Float::sum);
    }

    //returns sum of outputs:
    public static Float getOutputsValue(Transaction transaction) {
        return transaction.getOutputs()
                .stream()
                .map(TransactionOutput::getValue)
                .reduce((float) 0, Float::sum);
    }

    //Tacks in array of transactions and returns a merkle root.
    public static String getMerkleRoot(List<Transaction> transactions) {
        int count = transactions.size();

        List<String> previousTreeLayer = transactions.stream().map(Transaction::getTransactionId).collect(Collectors.toList());

        List<String> treeLayer = previousTreeLayer;
        while (count > 1) {
            treeLayer = new ArrayList<>();
            for (int i = 1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(SecurityUtil.applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }

}
