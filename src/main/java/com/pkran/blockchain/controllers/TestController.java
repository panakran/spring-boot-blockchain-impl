package com.pkran.blockchain.controllers;

import com.pkran.blockchain.models.Block;
import com.pkran.blockchain.services.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    ChainService chainService;

    @GetMapping("/blockchain")
    public List<Block> blockchain() {
        return chainService.getBlockchain();
    }

    @GetMapping("/valid")
    public Boolean isValid() {
        return chainService.isChainValid();
    }
}
