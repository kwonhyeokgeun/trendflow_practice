package com.trend.keyword.source.service;

import com.trend.keyword.source.dto.response.AddReqDto;
import com.trend.keyword.source.vo.response.FindAllByKeywordRes;
import com.trend.keyword.source.entity.Source;
import com.trend.keyword.source.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;

    public List<FindAllByKeywordRes> findAllByKeyword(String keword, LocalDateTime startDatetTime, LocalDateTime endDateTime){

        List<Source> sourceList=sourceRepository.findAllByKeyword(keword, startDatetTime, endDateTime);
        List<FindAllByKeywordRes> findAllByKeywordResList = new ArrayList<>();
        for(Source s : sourceList){
            findAllByKeywordResList.add(new FindAllByKeywordRes(s));
        }
        return findAllByKeywordResList;

    }
    public Source add(AddReqDto addReqDto){
        Source source = Source.builder()
                .title(addReqDto.getTitle())
                .content(addReqDto.getContent())
                .link(addReqDto.getLink())
                .thumbnail(addReqDto.getThumbnail())
                .reg_dt(addReqDto.getReg_dt())
                .build();

        return sourceRepository.save(source);

    }

}
