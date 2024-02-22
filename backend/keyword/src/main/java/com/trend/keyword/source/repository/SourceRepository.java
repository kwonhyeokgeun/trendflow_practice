package com.trend.keyword.source.repository;

import com.trend.keyword.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/*"SELECT s.source_id, s.origin_id, s.title, s.link, s.content, s.thumbnail, s.total_word_cnt, s.reg_dt " +
            "FROM source s " +
            "WHERE s.source_id in " +
            "( SELECT k.source_id " +
            "  FROM keyword k "+
            "  WHERE k.keyword = :keyword " +
            "  AND (k.reg_dt BETWEEN :startDateTime AND :endDateTime)) " +
            "ORDER BY k.importance DESC, s.reg_dt DESC " +
            "LIMIT 20; "
            */
public interface SourceRepository extends JpaRepository<Source, Long> {
    @Query(value = "SELECT s.source_id, s.origin_id, s.title, s.link, s.content, s.thumbnail, s.total_word_cnt, s.reg_dt " +
            "FROM source s " +
            "INNER JOIN " +
            "( SELECT * " +
            "  FROM keyword "+
            "  WHERE keyword = :keyword " +
            "  AND (reg_dt BETWEEN :startDateTime AND :endDateTime)"+
            "  ORDER BY importance DESC, reg_dt DESC LIMIT 20" +
            ") k " +
            "ON s.source_id = k.source_id " +
            "ORDER BY k.importance DESC, s.reg_dt DESC; "
            , nativeQuery = true)
    List<Source> findAllByKeyword(String keyword, LocalDateTime startDateTime,LocalDateTime endDateTime);
}
