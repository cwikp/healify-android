package com.healify.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
@AllArgsConstructor
public class DrugDTO {
    private String date;
    private String name;
    private String unit;
    private int quantity;
}
