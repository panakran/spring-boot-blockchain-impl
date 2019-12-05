package com.pkran.blockchain.controllers;

import com.pkran.blockchain.dtos.BlockDTO;
import com.pkran.blockchain.dtos.NewTransactionDTO;
import com.pkran.blockchain.services.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionsController {

    @Autowired
    ChainService chainService;

    @PostMapping("/transactions")
    public void createTransactions(@RequestBody List<NewTransactionDTO> newTransactionDTOList) {
        chainService.createTransactions(newTransactionDTOList);
    }

    @GetMapping("/transactions")
    public List<BlockDTO> getBlockchain() {
        return chainService.getBlockchain();
    }
}
