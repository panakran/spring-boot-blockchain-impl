package com.pkran.blockchain.models;

import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Transaction {

    private String transactionId; // this is also the hash of the transaction.
    private PublicKey sender; // senders address/public key.
    private PublicKey recipient; // Recipients address/public key.
    private Float value;
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

    private List<TransactionInput> inputs = new ArrayList<>();
    private List<TransactionOutput> outputs = new ArrayList<>();

    private String sequence;

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
        this.sequence = UUID.randomUUID().toString();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public void setRecipient(PublicKey recipient) {
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

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "Transaction{" + '\n' +
                "transactionId='" + transactionId + '\'' + '\n' +
                ", sender=" + sender + '\n' +
                ", recipient=" + recipient + '\n' +
                ", value=" + value + '\n' +
                ", signature=" + Arrays.toString(signature) + '\n' +
                ", inputs=" + inputs + '\n' +
                ", outputs=" + outputs + '\n' +
                ", sequence='" + sequence + '\'' + '\n' +
                '}';
    }
}