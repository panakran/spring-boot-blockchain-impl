package com.pkran.blockchain.dtos;

public class WalletDTO {

    private String username;
    private String publicKey;
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
