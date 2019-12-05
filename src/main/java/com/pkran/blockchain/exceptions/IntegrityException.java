package com.pkran.blockchain.exceptions;

public class IntegrityException extends RuntimeException {

    private String message;

    private Integer code;

    public IntegrityException(ErrorCode errorCode) {
        this.message = errorCode.name();
        this.code = errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
