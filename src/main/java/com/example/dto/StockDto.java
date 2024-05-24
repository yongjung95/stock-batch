package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {

    private String corpCode;

    private String corpName;

    private String dataModifyDate;

    private String stockCode;
}
