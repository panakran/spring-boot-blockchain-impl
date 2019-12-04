package com.pkran.blockchain.services;

import com.pkran.blockchain.Utilities.BlockchainUtil;
import com.pkran.blockchain.Utilities.SecurityUtil;
import com.pkran.blockchain.models.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.PublicKey;
import java.security.Security;
import java.util.*;

@Service
public class ChainService {

    public List<Block> blocks = new ArrayList<>();
    public List<Wallet> wallets = new ArrayList<>();
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //list of all unspent transactions.
    public static float minimumTransaction = 0.1f;

    @Value("${mine.difficulty}")
    public Integer DIFFICULTY;

    @Value("${coinbase.amount}")
    public Float COINBASE_AMOUNT;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

        //Create wallets:
        Wallet walletA = new Wallet();
        SecurityUtil.generateKeyPair(walletA);
        Wallet walletB = new Wallet();
        SecurityUtil.generateKeyPair(walletB);

        Wallet base = new Wallet();
        SecurityUtil.generateKeyPair(base);
        Wallet coinbase = new Wallet();
        SecurityUtil.generateKeyPair(coinbase);

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        Transaction genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, COINBASE_AMOUNT, null);
        BlockchainUtil.generateSignature(coinbase.privateKey, genesisTransaction);//manually sign the genesis transaction
        genesisTransaction.setTransactionId("0");//manually set the transaction id
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        addTransaction(genesis, genesisTransaction);
        addBlock(DIFFICULTY, genesis);

        Block block1 = new Block(genesis.getHash());
        System.out.println("\nWalletA's balance is: " + getBalance(walletA));
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        addTransaction(block1, sendFunds(walletA, walletB.publicKey, 40f));
        addBlock(DIFFICULTY, block1);
        System.out.println("\nWalletA's balance is: " + getBalance(walletA));
        System.out.println("WalletB's balance is: " + getBalance(walletB));

        isChainValid();

    }

    public List<Block> addBlock() {
        Block secondBlock = new Block(blocks.get(blocks.size() - 1).getHash());
        System.out.println("Hash for block 2 : " + secondBlock.getHash());
        blocks.add(secondBlock);
        return blocks;
    }

    public List<Block> getBlockchain() {
        return blocks;
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(blocks.get(0).getTransactions().get(0).getOutputs().get(0).getId(), blocks.get(0).getTransactions().get(0).getOutputs().get(0));

        //loop through blockchain to check hashes:
        for (int i = 1; i < blocks.size(); i++) {

            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(BlockchainUtil.calculateHash(currentBlock))) {
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.getHash().substring(0, DIFFICULTY).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (!BlockchainUtil.verifySignature(currentTransaction)) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if (!BlockchainUtil.getInputsValue(currentTransaction).equals(BlockchainUtil.getOutputsValue(currentTransaction))) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());

                    if (tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if (input.getUTXO().getValue() != tempOutput.getValue()) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public void addBlock(Integer difficulty, Block newBlock) {
        BlockchainUtil.mineBlock(difficulty, newBlock);
        blocks.add(newBlock);
    }


    //Returns true if new transaction could be created.
    public boolean processTransaction(Transaction transaction) {

        if (!BlockchainUtil.verifySignature(transaction)) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        transaction.getInputs()
                .forEach(trnsInput -> trnsInput.setUTXO(this.UTXOs.get(trnsInput.getTransactionOutputId())));

        //check if transaction is valid:
        if (BlockchainUtil.getInputsValue(transaction) < ChainService.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + BlockchainUtil.getInputsValue(transaction));
            return false;
        }

        //generate transaction outputs:
        Float leftOver = BlockchainUtil.getInputsValue(transaction) - transaction.getValue(); //get value of inputs then the left over change:
        transaction.setTransactionId(BlockchainUtil.calculateHash(transaction));
        transaction.getOutputs().add(new TransactionOutput(transaction.getRecipient(), transaction.getValue(), transaction.getTransactionId())); //send value to recipient
        transaction.getOutputs().add(new TransactionOutput(transaction.getSender(), leftOver, transaction.getTransactionId())); //send the left over 'change' back to sender

        //add outputs to Unspent list
        transaction.getOutputs().forEach(trnsOut -> this.UTXOs.put(trnsOut.getId(), trnsOut));

        //remove transaction inputs from UTXO lists as spent:
        transaction.getInputs().stream().filter(Objects::nonNull).forEach(i -> this.UTXOs.remove(i.getUTXO().getId()));

        return true;
    }


    //Add transactions to this block
    public boolean addTransaction(Block block, Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if ((!block.getPreviousHash().equals("0"))) {
            if ((!processTransaction(transaction))) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        block.getTransactions().add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }


    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public Float getBalance(Wallet wallet) {
        return this.UTXOs.values()
                .stream()
                .filter(uxto -> uxto.isMine(wallet.getPublicKey()))
                .peek(uxto -> wallet.getUTXOs().put(uxto.getId(), uxto))
                .map(TransactionOutput::getValue)
                .reduce((float) 0, Float::sum);
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(Wallet wallet, PublicKey recipient, Float value) {
        if (getBalance(wallet) < value) { //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        List<TransactionInput> inputs = new ArrayList<>();
        List<TransactionOutput> outputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : wallet.getUTXOs().entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if (total > value) break;
        }

        Transaction newTransaction = new Transaction(wallet.getPublicKey(), recipient, value, inputs);
        BlockchainUtil.generateSignature(wallet.getPrivateKey(), newTransaction);

        inputs.forEach(i -> wallet.getUTXOs().remove(i.getTransactionOutputId()));

        return newTransaction;
    }
}
