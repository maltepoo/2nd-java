package com.starters.applyservice.repository;

import com.starters.applyservice.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByRecruitmentEnd(LocalDateTime today);
    List<Lesson> findAllByRecruitmentStartAndRecruitmentEnd(LocalDate start, LocalDate end);
    List<Lesson> findAllByClassStartAndClassEnd(LocalDate start, LocalDate end);
}