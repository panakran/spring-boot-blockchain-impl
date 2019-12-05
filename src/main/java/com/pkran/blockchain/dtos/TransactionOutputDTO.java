package com.pkran.blockchain.dtos;

public class TransactionOutputDTO {

    private String id;
    private String recipient; //also known as the new owner of these coins.
    private Float value; //the amount of coins they own
    private String parentTransactionId; //the id of the transaction this output was created in

    public TransactionOutputDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }
}
