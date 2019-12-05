package com.pkran.blockchain.dtos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "TransactionDTO")
public class TransactionDTO {

    @ApiModelProperty(notes = "Transaction id")
    private String transactionId; // this is also the hash of the transaction.

    @ApiModelProperty(notes = "Sender public key")
    private String sender; // senders address/public key.

    @ApiModelProperty(notes = "Recipient public key")
    private String recipient; // Recipients address/public key.

    @ApiModelProperty(notes = "Amount")
    private Float value;

    @ApiModelProperty(notes = "Signature")
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

    @ApiModelProperty(notes = "Transaction inputs")
    private List<TransactionInputDTO> inputs = new ArrayList<>();

    @ApiModelProperty(notes = "Transaction outputs")
    private List<TransactionOutputDTO> outputs = new ArrayList<>();

    @ApiModelProperty(notes = "UUID")
    private String sequence;

    public TransactionDTO() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public List<TransactionInputDTO> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInputDTO> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutputDTO> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutputDTO> outputs) {
        this.outputs = outputs;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
