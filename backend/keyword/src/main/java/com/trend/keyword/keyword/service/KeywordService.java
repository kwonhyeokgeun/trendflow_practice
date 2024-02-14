package com.trend.keyword.keyword.service;

import com.trend.keyword.keyword.dto.response.FindAllByKeywordRes;
import com.trend.keyword.keyword.entity.Keyword;
import com.trend.keyword.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public List<FindAllByKeywordRes> findAllByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate){
        List<Keyword> keywordList = keywordRepository.findAllByKeywordAndRegDtBetween(keyword, startDate, endDate);
        //List<Keyword> keywordList = keywordRepository.findAllByKeyword(keyword);
        /*List<FindAllByKeywordRes> findAllByKeywordResList = new ArrayList<>();*/
        System.out.println(keywordList.size());
        for(Keyword keyword1 : keywordList){
            //FindAllByKeywordRes.builder().keywordId(keyword1.).build()
            System.out.println(keyword1);
        }
        return null;
    }

}
