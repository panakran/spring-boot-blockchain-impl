package com.pkran.blockchain.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "NewWalletDTO")
public class NewWalletDTO {

    @ApiModelProperty(notes = "New user username")
    private String username;

    @ApiModelProperty(notes = "Amount to add to new wallet")
    private Float amount;

    public NewWalletDTO(String username, Float amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
