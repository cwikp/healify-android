package com.healify.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
public class TemperatureDTO {

    private String date;
    private String value;
}
