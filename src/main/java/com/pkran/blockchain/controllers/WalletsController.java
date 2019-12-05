package com.pkran.blockchain.controllers;

import com.pkran.blockchain.dtos.NewWalletDTO;
import com.pkran.blockchain.dtos.WalletDTO;
import com.pkran.blockchain.services.WalletsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WalletsController {

    @Autowired
    WalletsService walletsService;

    @GetMapping("/wallets")
    public List<WalletDTO> getAllWallets() {
        return walletsService.getWallets();
    }

    @GetMapping("/wallets/{username}")
    public WalletDTO getWalletByUsername(@PathVariable String username) {
        return walletsService.getWallet(username);
    }

    @PostMapping("/wallets")
    public void createNewWallet(@RequestBody NewWalletDTO newWalletDTO) {
        walletsService.createWallet(newWalletDTO);
    }

}
