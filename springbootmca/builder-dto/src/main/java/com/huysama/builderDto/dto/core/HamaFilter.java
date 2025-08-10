package com.huysama.builderDto.dto.core;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HamaFilter<T> {
    private T filter;
    private Map<String, ParamBetween> bw;
    private Map<String, List<Object>> IN;
    private Map<String, List<Object>> INAnd;
    private Map<String, List<Object>> notIN;
    private List<String> isNull;
    private List<String> isNNull;
    private Map<String, String> isLike;
    private Map<String, String> isNLike;
    private List<HamaFilter<T>> subQueries;
    private Map<String,String[]> sort;
    private Long limit;
    private Map<String, Object> notEqual;
    private Map<String, String> equalIgnoreCase;
}
