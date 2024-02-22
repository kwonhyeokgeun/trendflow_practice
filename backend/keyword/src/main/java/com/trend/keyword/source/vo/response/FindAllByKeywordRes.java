package com.trend.keyword.source.vo.response;

import com.trend.keyword.source.entity.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindAllByKeywordRes {
    private Long sourceId;
    private String title;
    private String link;
    private String content;
    private String thumbnail;
    private int totalWordCnt;

    public FindAllByKeywordRes(Source source){
        sourceId = source.getSourceId();
        title = source.getTitle();
        link = source.getLink();
        content = source.getContent();
        thumbnail = source.getThumbnail();
    }
}
