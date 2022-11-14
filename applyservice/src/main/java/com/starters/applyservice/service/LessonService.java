package com.starters.applyservice.service;

import org.springframework.stereotype.Service;

@Service
public class LessonService {

    public static String findAllClass() {
        return "모든 클래스 조회";
    }

    public static String deleteClass(Long classId) {
        return "특정 클래스 삭제";
    }

    public static String updateClass(Long classId) {
        return "클래스 내용 수정";
    }

    public static String updateClassStatus(Long classId, String status) {
        return "클래스 모집기간만 수정";
    }
}
