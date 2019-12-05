package com.pkran.blockchain.controllers;

import com.pkran.blockchain.services.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockchainController {

    @Autowired
    ChainService chainService;

    @GetMapping("/valid")
    public void isValid() {
        chainService.isChainValid();
    }
}
