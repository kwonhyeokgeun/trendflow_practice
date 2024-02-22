package com.trend.keyword.source.entity;

import com.trend.keyword.keyword.entity.Keyword;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name="source")
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sourceId;
    private Long originId;
    private String title;
    private String link;
    private String content;
    private String thumbnail;
    private int totalWordCnt;
    private LocalDateTime reg_dt;

    /*@OneToMany(fetch = FetchType.LAZY)
    //@JoinColumn(name = "source_id")
    private List<Keyword> keywordList;*/
}
