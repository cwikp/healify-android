package com.healify.web.dto;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class HealthStateDTO {
    private int temperature;
    private int pressure;
}
