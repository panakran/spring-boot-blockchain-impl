package com.pkran.blockchain.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "BlockDTO")
public class BlockDTO {

    @ApiModelProperty(notes = "Block hash")
    private String hash;

    @ApiModelProperty(notes = "Previous block hash")
    private String previousHash;

    @ApiModelProperty(notes = "Merkle root hash (keep reference to previous blocks)")
    private String merkleRoot;
    @ApiModelProperty(notes = "Transactions")
    private List<TransactionDTO> transactions = new ArrayList<>();

    @ApiModelProperty(notes = "Generated timestamp")
    private Long timeStamp;

    @ApiModelProperty(notes = "Nonce(mining proof)")
    private Integer nonce;

    public BlockDTO() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }
}
