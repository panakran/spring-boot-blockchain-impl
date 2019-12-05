package com.pkran.blockchain.controllers;

import com.pkran.blockchain.services.ChainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Api(value = "Blockchain")
public class BlockchainController {

    @Autowired
    ChainService chainService;

    @ApiOperation(value = "Check if the blockchain is valid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Blockchain is valid"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/valid")
    public void isValid() {
        chainService.isChainValid();
    }
}
