package com.common.oauth.exception;


import com.common.exception.ErrorCodes;

public enum ErrorCode implements ErrorCodes {

    UNAUTHORIZED("COMMON_AUTH_001"),
    NOT_NULL_VALIDATION("COMMON_AUTH_002"),
    INVALID_TOKEN_FORMAT("COMMON_AUTH_003"),
    TOKEN_EXPIRED("COMMON_AUTH_004"),
    AUTHENTICATION_FAILED("COMMON_AUTH_005"),
    VALIDATION_ERROR("COMMON_AUTH_006"),;

    private final String value;

    ErrorCode(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getDefaultMessage() {
        return "";
    }
}
