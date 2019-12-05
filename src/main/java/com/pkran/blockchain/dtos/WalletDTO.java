package com.pkran.blockchain.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "WalletDTO")
public class WalletDTO {

    @ApiModelProperty(notes = "Username")
    private String username;

    @ApiModelProperty(notes = "Public key string")
    private String publicKey;

    @ApiModelProperty(notes = "Balance")
    private Float balance;

    public WalletDTO(String username, String publicKey, Float balance) {
        this.username = username;
        this.publicKey = publicKey;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
}
