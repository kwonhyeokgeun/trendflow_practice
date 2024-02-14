package com.trend.keyword.keyword.repository;

import com.trend.keyword.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword,Long> {
    List<Keyword> findAllByKeywordAndRegDtBetween(String keyword, LocalDateTime startDate, LocalDateTime endDate);
    List<Keyword> findAllByKeyword(String keyword);
}
