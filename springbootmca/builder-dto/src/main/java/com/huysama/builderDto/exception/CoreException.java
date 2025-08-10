package com.huysama.builderDto.exception;

import lombok.Getter;

@Getter
public class CoreException extends GlobalException {
    int statusCode;
    public CoreException(int i, String message) {
        super(message);
        this.statusCode = i;
    }
}
