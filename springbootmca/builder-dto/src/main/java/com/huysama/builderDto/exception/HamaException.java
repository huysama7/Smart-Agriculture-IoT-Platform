package com.huysama.builderDto.exception;

import lombok.Getter;

@Getter
public class HamaException extends GlobalException {
    private final String errorCode;
    private final int code;
    private final String errorMessage;

    public HamaException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.code = 500;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public HamaException(int code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorCode = "";
        this.errorMessage = errorMessage;
    }
}
