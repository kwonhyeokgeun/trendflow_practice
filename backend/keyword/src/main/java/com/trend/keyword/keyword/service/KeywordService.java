package com.trend.keyword.keyword.service;

import com.trend.keyword.keyword.dto.response.FindAllByKeywordRes;
import com.trend.keyword.keyword.entity.Keyword;
import com.trend.keyword.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public List<FindAllByKeywordRes> findAllByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate){
        List<Keyword> keywordList = keywordRepository.findAllByKeywordAndRegDtBetweenOrderByImportanceDesc(keyword, startDate, endDate);
        List<FindAllByKeywordRes> findAllByKeywordResList = new ArrayList<>();
        for(Keyword keyword1 : keywordList){
            findAllByKeywordResList.add(
                FindAllByKeywordRes.builder()
                    .sourceId(keyword1.getSource().getSourceId())
                    .keyword(keyword1.getKeyword())
                    .count(keyword1.getCount())
                    .importance(keyword1.getImportance())
                    .regDt(keyword1.getRegDt())
                    .build()
            );
            //System.out.println(keyword1);
        }
        return findAllByKeywordResList;
    }

}
