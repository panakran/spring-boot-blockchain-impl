package com.pkran.blockchain.services;

import com.pkran.blockchain.Utilities.BlockchainUtil;
import com.pkran.blockchain.Utilities.SecurityUtil;
import com.pkran.blockchain.Utilities.StringUtil;
import com.pkran.blockchain.dtos.BlockDTO;
import com.pkran.blockchain.dtos.NewTransactionDTO;
import com.pkran.blockchain.exceptions.ErrorCode;
import com.pkran.blockchain.exceptions.IntegrityException;
import com.pkran.blockchain.mappers.Mapper;
import com.pkran.blockchain.models.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.security.Security;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChainService {

    public List<Block> blocks = new ArrayList<>();
    public HashMap<String, Wallet> wallets = new HashMap<>();
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //list of all unspent transactions.
    public static float minimumTransaction = 0.1f;

    @Value("${mine.difficulty}")
    public Integer DIFFICULTY;

    @Value("${coinbase.amount}")
    public Float COINBASE_AMOUNT;

    @Value("${coinbase.amount.b}")
    public BigDecimal COINBASE_AMOUNTB;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider()); //Setup Bouncy castle as a Security Provider

        Wallet walletA = new Wallet();
        SecurityUtil.generateKeyPair(walletA);
        wallets.put("test1", walletA);
        Wallet walletB = new Wallet();
        SecurityUtil.generateKeyPair(walletB);
        wallets.put("test2", walletB);

        Wallet base = new Wallet();
        SecurityUtil.generateKeyPair(base);
        Wallet coinbase = new Wallet();
        SecurityUtil.generateKeyPair(coinbase);
        wallets.put("coinbase", coinbase);

        Transaction genesisTransaction = new Transaction(base.getPublicKey(), coinbase.getPublicKey(), COINBASE_AMOUNT, null);
        BlockchainUtil.generateSignature(coinbase.getPrivateKey(), genesisTransaction);//manually sign the genesis transaction
        genesisTransaction.setTransactionId("0");//manually set the transaction id
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId())); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0)); //its important to store our first transaction in the UTXOs list.
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        addTransaction(genesis, genesisTransaction);
        addBlock(DIFFICULTY, genesis);

        Block block1 = new Block(blocks.get(blocks.size() - 1).getHash());
        System.out.println("\ncoinbase's balance is: " + getBalance(coinbase));
        System.out.println("\ncoinbase is Attempting to send funds (1000) to WalletA...");
        addTransaction(block1, sendFunds(coinbase, walletA.getPublicKey(), 1000f));
        addBlock(DIFFICULTY, block1);
        System.out.println("\ncoinbase's balance is: " + getBalance(coinbase));
        System.out.println("WalletA's balance is: " + getBalance(walletA));

        Block block2 = new Block(blocks.get(blocks.size() - 1).getHash());
        System.out.println("\nWalletA's balance is: " + getBalance(walletA));
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        addTransaction(block1, sendFunds(walletA, walletA.getPublicKey(), 40f));
        addBlock(DIFFICULTY, block2);
        System.out.println("\nWalletA's balance is: " + getBalance(walletA));
        System.out.println("WalletB's balance is: " + getBalance(walletB));

        isChainValid();

    }

    public List<BlockDTO> getBlockchain() {
        return blocks.subList(1, blocks.size() - 1).stream().map(Mapper::map).collect(Collectors.toList());
    }

    public void createTransactions(List<NewTransactionDTO> newTransactionDTOList) {
        Block block = new Block(blocks.get(blocks.size() - 1).getHash());
        newTransactionDTOList
                .stream()
                .map(t -> sendFunds(wallets.get(t.getSender()), wallets.get(t.getReceiver()).getPublicKey(), t.getAmount()))
                .forEach(t -> addTransaction(block, t));
        addBlock(DIFFICULTY, block);
    }

    public void isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = StringUtil.createZerosStringFromInteger(DIFFICULTY);
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(blocks.get(0).getTransactions().get(0).getOutputs().get(0).getId(), blocks.get(0).getTransactions().get(0).getOutputs().get(0));

        //loop through blockchain to check hashes:
        for (int i = 1; i < blocks.size(); i++) {

            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(BlockchainUtil.calculateHash(currentBlock))) {
                throw new IntegrityException(ErrorCode.CURRENT_HASHES_NOT_EQUAL);
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                throw new IntegrityException(ErrorCode.PREVIOUS_HASHES_NOT_EQUAL);
            }
            //check if hash is solved
            if (!currentBlock.getHash().substring(0, DIFFICULTY).equals(hashTarget)) {
                throw new IntegrityException(ErrorCode.BLOCK_NOT_MINED);
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if (!BlockchainUtil.verifySignature(currentTransaction)) {
                    throw new IntegrityException(ErrorCode.TRANSACTION_SIGNATURE_INVALID);
                }
                if (!BlockchainUtil.getInputsValue(currentTransaction).equals(BlockchainUtil.getOutputsValue(currentTransaction))) {
                    throw new IntegrityException(ErrorCode.INPUTS_NOT_EQUAL_TO_OUTPUTS);
                }

                for (TransactionInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.getTransactionOutputId());

                    if (tempOutput == null) {
                        throw new IntegrityException(ErrorCode.TRANSACTION_REFERENCE_INPUT_MISSING);
                    }

                    if (!input.getUTXO().getValue().equals(tempOutput.getValue())) {
                        throw new IntegrityException(ErrorCode.TRANSACTION_REFERENCE_VALUE_INVALID);
                    }

                    tempUTXOs.remove(input.getTransactionOutputId());
                }

                for (TransactionOutput output : currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.getId(), output);
                }

                if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                    throw new IntegrityException(ErrorCode.OUTPUT_RECIPIENT_IS_INVALID);
                }
                if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                    throw new IntegrityException(ErrorCode.OUTPUT_SENDER_IS_INVALID);
                }

            }

        }
        System.out.println("Blockchain is valid");
    }

    public void addBlock(Integer difficulty, Block newBlock) {
        BlockchainUtil.mineBlock(difficulty, newBlock);
        blocks.add(newBlock);
    }


    //Returns true if new transaction could be created.
    public boolean processTransaction(Transaction transaction) {

        if (!BlockchainUtil.verifySignature(transaction)) {
            throw new IntegrityException(ErrorCode.SIGNATURE_FAILED_TO_VERIFY);
        }

        //gather transaction inputs (Make sure they are unspent):
        transaction.getInputs()
                .forEach(trnsInput -> trnsInput.setUTXO(this.UTXOs.get(trnsInput.getTransactionOutputId())));

        //check if transaction is valid:
        if (BlockchainUtil.getInputsValue(transaction) < ChainService.minimumTransaction) {
            throw new IntegrityException(ErrorCode.TRANSACTION_INPUT_TO_SMALL);
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
                throw new IntegrityException(ErrorCode.TRANSACTION_FAILED_TO_PROCESS);
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
            throw new IntegrityException(ErrorCode.NOT_ENOUGH_FUNDS);
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
