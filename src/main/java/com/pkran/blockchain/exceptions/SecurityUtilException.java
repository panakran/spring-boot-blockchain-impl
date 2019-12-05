package com.pkran.blockchain.exceptions;

public class SecurityUtilException extends RuntimeException {

    public SecurityUtilException(Throwable t) {
        super(t.getMessage());
    }

}
