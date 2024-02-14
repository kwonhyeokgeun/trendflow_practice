package com.trend.keyword.source.controller;

import com.trend.keyword.source.dto.response.AddReqDto;
import com.trend.keyword.source.entity.Source;
import com.trend.keyword.source.service.SourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/source")
public class SourceController {

    private final SourceService sourceService;
    /*@GetMapping("")
    public ResponseEntity<Object> findAllByKeyword(@RequestParam String title,
                                                   @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                   @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("findRecommend - Call");
        try {
            endDate = endDate.plusDays(1);

            return ResponseEntity.ok().body(null);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }*/

    @PostMapping("")
    public ResponseEntity<Object> add(@RequestBody AddReqDto addReqDto){
        log.info("add - Call");
        try{
            sourceService.add(addReqDto);
        }catch (RuntimeException e){
            return null;
        }
        return null;
    }

}
