package com.huysama.builderDto.config;

import com.huysama.builderDto.dto.system.ErrorResponse;
import com.huysama.builderDto.exception.CoreException;
import com.huysama.builderDto.exception.GlobalException;
import com.huysama.builderDto.exception.HamaException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
public class CoreGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        String uuid = UUID.randomUUID().toString();
        int status;
        String message;
        Map<String, Object> message_json = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException validationException) {
            validationException.getBindingResult().getFieldErrors().forEach(error ->
                    message_json.put(error.getField(), error.getDefaultMessage())
            );
            message = "Validation failed for one or more fields";
            status = validationException.getStatusCode().value();
        } else if (ex instanceof CoreException coreException) {
            message = coreException.getMessage();
            status = coreException.getStatusCode();
        } else if (ex instanceof HamaException customException) {
            message = customException.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else if (ex instanceof GlobalException globalException) {
            message = globalException.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else {
            message = ex.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

//        log.error("[{}] Exception occurred: {}", uuid, message, ex);
        log.error("[{}] Exception occurred: {}", uuid, ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(uuid, status, message, message_json,new Date());
        return new ResponseEntity<>(response, HttpStatus.valueOf(status));
    }


}
