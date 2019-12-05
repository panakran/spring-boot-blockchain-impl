package com.pkran.blockchain.services;

import com.pkran.blockchain.Utilities.SecurityUtil;
import com.pkran.blockchain.dtos.NewWalletDTO;
import com.pkran.blockchain.dtos.WalletDTO;
import com.pkran.blockchain.models.Block;
import com.pkran.blockchain.models.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletsService {

    @Autowired
    ChainService chainService;


    public void createWallet(NewWalletDTO newWalletDTO) {
        if (chainService.wallets.containsKey(newWalletDTO.getUsername()))
            throw new RuntimeException("User already exists");

        Wallet wallet = new Wallet();
        SecurityUtil.generateKeyPair(wallet);

        Wallet coinbase = chainService.wallets.get("coinbase");

        Block block = new Block(chainService.blocks.get(chainService.blocks.size() - 1).getHash());
        System.out.println("\ncoinbase's balance is: " + chainService.getBalance(coinbase));
        System.out.println("\ncoinbase is Attempting to send " + newWalletDTO.getAmount() + "funds to" + newWalletDTO.getUsername());
        chainService.addTransaction(block, chainService.sendFunds(coinbase, wallet.getPublicKey(), newWalletDTO.getAmount()));
        chainService.addBlock(chainService.DIFFICULTY, block);
        System.out.println("\ncoinbase's balance is: " + chainService.getBalance(coinbase));
        System.out.println(newWalletDTO.getUsername() + "Wallet's balance is: " + chainService.getBalance(wallet));
        chainService.wallets.put(newWalletDTO.getUsername(), wallet);
    }

    public List<WalletDTO> getWallets() {
        return chainService.wallets.entrySet().stream().map(e -> new WalletDTO(e.getKey(), e.getValue().getPublicKey().toString(), chainService.getBalance(e.getValue()))).collect(Collectors.toList());
    }

    public WalletDTO getWallet(String username) {
        Wallet responseWallet = chainService.wallets.get(username);
        return new WalletDTO(username, responseWallet.getPublicKey().toString(), chainService.getBalance(responseWallet));
    }
}
