package com.trend.keyword.source.service;

import com.trend.keyword.source.dto.response.AddReqDto;
import com.trend.keyword.source.entity.Source;
import com.trend.keyword.source.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SourceService {
    private final SourceRepository sourceRepository;

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
