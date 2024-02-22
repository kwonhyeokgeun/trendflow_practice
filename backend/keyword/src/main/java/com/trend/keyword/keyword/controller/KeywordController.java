package com.trend.keyword.keyword.controller;

import com.trend.keyword.keyword.dto.request.FindAllByKeywordReq;
import com.trend.keyword.keyword.dto.response.FindAllByKeywordRes;
import com.trend.keyword.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/keyword")
public class KeywordController {
    private final KeywordService keywordService;
    @GetMapping("/test")
    public ResponseEntity<Object>test(){
        log.info("test - Call");
        return ResponseEntity.ok().body(null);
    }
    @GetMapping("")
    public ResponseEntity<Object> findAllByKeyword(@RequestParam String keyword,
                                               @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                               @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
        log.info("findAllByKeyword - Call");

        try {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            List<FindAllByKeywordRes> ret = keywordService.findAllByKeyword(keyword,startDateTime,endDateTime);
            return ResponseEntity.ok().body(ret);
        } catch (RuntimeException e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
