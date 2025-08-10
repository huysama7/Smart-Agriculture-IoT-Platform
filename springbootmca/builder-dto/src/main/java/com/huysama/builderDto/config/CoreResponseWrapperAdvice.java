package com.huysama.builderDto.config;

import com.huysama.builderDto.dto.system.ErrorResponse;
import com.huysama.builderDto.dto.system.SuccessResponse;
import com.huysama.builderDto.util.HamaUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public class CoreResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Chỉ áp dụng cho RestController, không áp dụng cho các loại khác (vd: String, file download...)
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // Kiểm tra nếu body là null, trả về một đối tượng ResponseEntity rỗng
        if (body instanceof SuccessResponse<?> || body instanceof ErrorResponse) {
            return body; // Nếu đã là SuccessResponse, không cần wrap lại
        }
        // Nếu body là String, wrap rồi serialize sang JSON bằng Gson
        if (body instanceof String) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return HamaUtil.stringify(SuccessResponse.of(body));
        }
        return SuccessResponse.of(body);
    }
}
