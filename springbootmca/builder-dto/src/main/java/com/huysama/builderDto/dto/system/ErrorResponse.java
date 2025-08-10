package com.huysama.builderDto.dto.system;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String uuid;
    private int status;
    private String message;
    private Map<String,Object> message_json;
    private Date timestamp;
}
