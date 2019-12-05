package com.pkran.blockchain.controllers;

import com.pkran.blockchain.dtos.BlockDTO;
import com.pkran.blockchain.dtos.NewTransactionDTO;
import com.pkran.blockchain.exceptions.IntegrityException;
import com.pkran.blockchain.services.ChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
public class TransactionsController {

    @Autowired
    ChainService chainService;

    @ExceptionHandler(IntegrityException.class)
    @PostMapping("/transactions")
    public void createTransactions(@RequestBody List<NewTransactionDTO> newTransactionDTOList) {
        chainService.createTransactions(newTransactionDTOList);
    }

    @GetMapping("/transactions")
    public List<BlockDTO> getBlockchain() {
        return chainService.getBlockchain();
    }
}
