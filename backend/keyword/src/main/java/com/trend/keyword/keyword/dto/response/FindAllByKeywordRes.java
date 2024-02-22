package com.trend.keyword.keyword.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindAllByKeywordRes {
    Long keywordId;
    Long sourceId;
    String keyword;
    int count;
    int importance;
    LocalDateTime regDt;

}
