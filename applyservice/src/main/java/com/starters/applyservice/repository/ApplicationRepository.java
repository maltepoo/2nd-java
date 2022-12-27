package com.starters.applyservice.repository;

import com.starters.applyservice.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByMemberId(Long memberId);
    List<Application> findAllByLessonId(Long lessonId);
    List<Application> findAllByMemberNameAndStatusIs(String member_name, Integer status);
    List<Application> findAllByMemberIdAndLessonId(Long memberId, Long lessonId);
}