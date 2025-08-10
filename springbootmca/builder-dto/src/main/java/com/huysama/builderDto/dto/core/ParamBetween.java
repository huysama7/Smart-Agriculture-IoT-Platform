package com.huysama.builderDto.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamBetween {
    private Comparable<?> start;
    private Comparable<?> end;
}
