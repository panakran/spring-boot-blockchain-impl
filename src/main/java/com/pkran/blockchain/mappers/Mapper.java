package com.pkran.blockchain.mappers;

import com.pkran.blockchain.dtos.BlockDTO;
import com.pkran.blockchain.dtos.TransactionDTO;
import com.pkran.blockchain.dtos.TransactionInputDTO;
import com.pkran.blockchain.dtos.TransactionOutputDTO;
import com.pkran.blockchain.models.Block;
import com.pkran.blockchain.models.Transaction;
import com.pkran.blockchain.models.TransactionInput;
import com.pkran.blockchain.models.TransactionOutput;

import java.util.stream.Collectors;

public final class Mapper {

    public static BlockDTO map(Block block) {
        BlockDTO blockDTO = new BlockDTO();
        blockDTO.setHash(block.getHash());
        blockDTO.setMerkleRoot(block.getMerkleRoot());
        blockDTO.setNonce(block.getNonce());
        blockDTO.setPreviousHash(block.getPreviousHash());
        blockDTO.setTimeStamp(block.getTimeStamp());
        blockDTO.setTransactions(block.getTransactions().stream().map(Mapper::map).collect(Collectors.toList()));
        return blockDTO;
    }

    public static TransactionInputDTO map(TransactionInput transactionInput) {
        TransactionInputDTO transactionInputDTO = new TransactionInputDTO();
        transactionInputDTO.setTransactionOutputId(transactionInput.getTransactionOutputId());
        transactionInputDTO.setUTXO(map(transactionInput.getUTXO()));
        return transactionInputDTO;
    }

    public static TransactionDTO map(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setRecipient(transaction.getRecipient().toString());
        transactionDTO.setSender(transaction.getSender().toString());
        transactionDTO.setSequence(transaction.getSequence());
        transactionDTO.setSignature(transaction.getSignature());
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setValue(transaction.getValue());
        transactionDTO.setInputs(transaction.getInputs().stream().map(Mapper::map).collect(Collectors.toList()));
        transactionDTO.setOutputs(transaction.getOutputs().stream().map(Mapper::map).collect(Collectors.toList()));
        return transactionDTO;
    }

    private static TransactionOutputDTO map(TransactionOutput transactionOutput) {
        TransactionOutputDTO transactionOutputDTO = new TransactionOutputDTO();
        transactionOutputDTO.setId(transactionOutput.getId());
        transactionOutputDTO.setParentTransactionId(transactionOutput.getParentTransactionId());
        transactionOutputDTO.setRecipient(transactionOutput.getRecipient().toString());
        transactionOutputDTO.setValue(transactionOutput.getValue());
        return transactionOutputDTO;
    }
}
