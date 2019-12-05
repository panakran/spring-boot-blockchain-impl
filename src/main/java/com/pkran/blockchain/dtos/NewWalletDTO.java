package com.pkran.blockchain.dtos;

public class NewWalletDTO {
    private String username;
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
