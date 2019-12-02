package com.pkran.blockchain.Utilities;

import com.pkran.blockchain.models.Block;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Service
public class BlockchainUtil {

    //Applies Sha256 to a string and returns the result.
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input,

            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexidecimal
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
        return BlockchainUtil.applySha256(
                block.getPreviousHash() +
                        block.getTimeStamp() +
                        block.getNonce() +
                        block.getData()
        );
    }

    public static void mineBlock(Integer difficulty, Block block) {
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        while(!block.getHash().substring( 0, difficulty).equals(target)) {
            block.setNonce(block.getNonce()+1);
            block.setHash(BlockchainUtil.calculateHash(block));
        }
        System.out.println("Block Mined!!! : " + block.getHash());
    }

}
