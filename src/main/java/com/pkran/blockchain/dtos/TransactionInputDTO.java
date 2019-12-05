package com.pkran.blockchain.dtos;

public class TransactionInputDTO {
    private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    private TransactionOutputDTO UTXO;

    public TransactionInputDTO() {
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public TransactionOutputDTO getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutputDTO UTXO) {
        this.UTXO = UTXO;
    }
}
