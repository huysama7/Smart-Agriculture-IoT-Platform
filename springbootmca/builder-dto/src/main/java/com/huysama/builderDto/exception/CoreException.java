package com.huysama.builderDto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CoreException extends GlobalException {
    int statusCode;
    public CoreException(int i, String message) {
        super(message);
        this.statusCode = i;
    }
    public CoreException(HttpStatus i, String message) {
        super(message);
        this.statusCode = i.value();
    }
}
