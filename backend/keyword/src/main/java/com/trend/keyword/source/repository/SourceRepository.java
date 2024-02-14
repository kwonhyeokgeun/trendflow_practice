package com.trend.keyword.source.repository;

import com.trend.keyword.source.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Long> {

}
