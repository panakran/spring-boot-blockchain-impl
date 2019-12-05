package com.pkran.blockchain.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "TransactionInputDTO")
public class TransactionInputDTO {

    @ApiModelProperty(notes = "Transaction output id (TransactionOutputs -> transactionId)")
    private String transactionOutputId; //Reference to TransactionOutputs -> transactionId

    @ApiModelProperty(notes = "TransactionOutputDTO")
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
