package com.starters.applyservice.controller;

import com.starters.applyservice.service.LessonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/class")
public class LessonController {

    @GetMapping("")
    public String getAllClass() {
        return LessonService.findAllClass();
    }

    @PutMapping("")
    public String updateClass(Long classId) {
        return LessonService.updateClass(classId);
    }

    @DeleteMapping("")
    public String deleteClass(Long classId) {
        return LessonService.deleteClass(classId);
    }

    @PutMapping("/status")
    public String updateClassStatus(Long classId, String status) {
        return LessonService.updateClassStatus(classId, status);
    }
}
