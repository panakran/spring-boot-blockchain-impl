package com.pkran.blockchain.exceptions;

public enum ErrorCode {

    CURRENT_HASHES_NOT_EQUAL(1),
    PREVIOUS_HASHES_NOT_EQUAL(2),
    BLOCK_NOT_MINED(3),
    TRANSACTION_SIGNATURE_INVALID(4),
    INPUTS_NOT_EQUAL_TO_OUTPUTS(5),
    TRANSACTION_REFERENCE_INPUT_MISSING(6),
    TRANSACTION_REFERENCE_VALUE_INVALID(7),
    OUTPUT_RECIPIENT_IS_INVALID(8),
    OUTPUT_SENDER_IS_INVALID(9),
    SIGNATURE_FAILED_TO_VERIFY(10),
    TRANSACTION_INPUT_TO_SMALL(11),
    TRANSACTION_FAILED_TO_PROCESS(12),
    NOT_ENOUGH_FUNDS(13);

    private final int code;

    ErrorCode(Integer code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
