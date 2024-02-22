package com.trend.keyword.source.service;

import com.trend.keyword.source.dto.response.AddReqDto;
import com.trend.keyword.source.entity.Source;
import com.trend.keyword.source.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;

    public void findAllByKeyword(String keword, LocalDateTime startDatetTime, LocalDateTime endDateTime){

        List<Source> sourceList=sourceRepository.findAllByKeyword(keword, startDatetTime, endDateTime);
        for(Source s : sourceList){
            System.out.println(s.getSourceId());
        }

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
