package com.pkran.blockchain.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "BlockDTO")
public class NewTransactionDTO {

    @ApiModelProperty(notes = "Sender username")
    private String sender;

    @ApiModelProperty(notes = "Receiver username")
    private String receiver;

    @ApiModelProperty(notes = "Amount transferred")
    private Float amount;

    public NewTransactionDTO(String sender, String receiver, Float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
