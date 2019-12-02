package com.pkran.blockchain.services;

import com.pkran.blockchain.Utilities.BlockchainUtil;
import com.pkran.blockchain.models.Block;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChainService {

    List<Block> blocks = new ArrayList<>();


    @PostConstruct
    public void init(){
        Block genesisBlock = new Block("Hi im the first block", "0");
        System.out.println("Genesis block hash: " + genesisBlock.getHash());
        blocks.add(genesisBlock);
    }

    public List<Block> addBlock(){
        Block secondBlock = new Block("Yo im the second block",blocks.get(blocks.size()-1).getHash());
        System.out.println("Hash for block 2 : " + secondBlock.getHash());
        blocks.add(secondBlock);
        return blocks;
    }

    public List<Block> getBlockchain(){
        return blocks;
    }

    public Boolean isChainValid(){
        return BlockchainUtil.isChainValid(this.blocks);
    }

}
