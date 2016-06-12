package com.healify.web.dto;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class CheckUpDTO {
    private String name;
    private String result;
    private String date;
}
