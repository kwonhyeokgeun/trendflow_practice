package com.trend.keyword.keyword.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FindAllByKeywordReq {
    private String keyword;
    private LocalDate startDate;
    private LocalDate endDate;
}
