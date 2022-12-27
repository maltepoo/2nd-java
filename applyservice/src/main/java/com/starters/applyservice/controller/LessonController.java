package com.starters.applyservice.controller;

import com.starters.applyservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("")
    public String getAllClass() {
        return lessonService.findAllClass();
    }

    @PutMapping("")
    public String updateClass(Long classId) {
        return lessonService.updateClass(classId);
    }

    @DeleteMapping("")
    public String deleteClass(Long classId) {
        return lessonService.deleteClass(classId);
    }

    @PutMapping("/status")
    public String updateClassStatus(Long classId, String status) {
        return lessonService.updateClassStatus(classId, status);
    }
}
