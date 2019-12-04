package com.pkran.blockchain.Utilities;

import com.pkran.blockchain.models.*;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.*;

@Service
public class BlockchainUtil {

    public static Boolean isChainValid(List<Block> chain) {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for (int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(calculateHash(currentBlock))) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }

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
                SecurityUtil.getStringFromKey(transaction.getSender()) +
                        SecurityUtil.getStringFromKey(transaction.getRecipient()) +
                        Float.toString(transaction.getValue()) + UUID.randomUUID().toString()
        );
    }

    public static void mineBlock(Integer difficulty, Block block) {
        block.setMerkleRoot(BlockchainUtil.getMerkleRoot(block.getTransactions()));
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.setNonce(block.getNonce() + 1);
            block.setHash(BlockchainUtil.calculateHash(block));
        }
        System.out.println("Block Mined!!! : " + block.getHash());

    }

    //Signs all the data we dont wish to be tampered with.
    public static void generateSignature(PrivateKey privateKey, Transaction transaction) {
        String data = SecurityUtil.getStringFromKey(transaction.getSender()) + SecurityUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
        transaction.setSignature(SecurityUtil.applyECDSASig(privateKey, data));
    }

    //Verifies the data we signed hasnt been tampered with
    public static boolean verifySignature(Transaction transaction) {
        String data = SecurityUtil.getStringFromKey(transaction.getSender()) + SecurityUtil.getStringFromKey(transaction.getRecipient()) + Float.toString(transaction.getValue());
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
    public static String getMerkleRoot(ArrayList<Transaction> transactions) {
        int count = transactions.size();
        List<String> previousTreeLayer = new ArrayList<>();
        for (Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.getTransactionId());
        }
        List<String> treeLayer = previousTreeLayer;
        while (count > 1) {
            treeLayer = new ArrayList<String>();
            for (int i = 1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(SecurityUtil.applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }

}
