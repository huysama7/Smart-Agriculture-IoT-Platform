package com.huysama.builderDto.dto.system;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponse<T> {
    private String uuid;
    private Date timestamp;
    private int status;
    private String code;
    private T result;
    private Map<String, Object> metadata;

    public static <T> SuccessResponse<T> of(int status, String code, Map<String, Object> metadata, T result) {
        return SuccessResponse.<T>builder()
                .uuid(UUID.randomUUID().toString())
                .timestamp(new Date())
                .status(status)
                .code(code)
                .result(result)
                .metadata(metadata != null ? metadata : new HashMap<>())
                .build();
    }

    public static <T> SuccessResponse<T> of(T result) {
        return SuccessResponse.of(200, "success", null, result);
    }

    public static <T> SuccessResponse<T> of(int status, T result) {
        return SuccessResponse.of(status, "success", null, result);
    }
}
