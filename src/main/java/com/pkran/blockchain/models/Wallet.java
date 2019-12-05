package com.pkran.blockchain.models;

import java.security.*;
import java.util.HashMap;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); //only UTXOs owned by this wallet.

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public HashMap<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }

    public void setUTXOs(HashMap<String, TransactionOutput> UTXOs) {
        this.UTXOs = UTXOs;
    }
}