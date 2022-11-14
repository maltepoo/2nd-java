package com.starters.applyservice.controller;


import com.starters.applyservice.domain.Application;
import com.starters.applyservice.service.ApplicationService;
import com.starters.applyservice.vo.ApplicationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("")
    public String getAllApplications() {
        // 모든 지원서를 조회 ✅
        return applicationService.findAllApplications().toString();
    }

    @PostMapping("")
    public String application(@RequestBody ApplicationVo data, Long memberId, Long classId) {
        // 클래스에 해당하는 지원서를 작성 ✅
        log.info(data.toString());
        return applicationService.createApplication(data, memberId, classId);
    }

    @GetMapping("/{applicationId}")
    public String getApplication(@PathVariable Long applicationId) {
        // 특정 지원서 조회 ✅
        return applicationService.findApplication(applicationId);
    }

    @PutMapping("/{applicationId}")
    public String updateApplication(@RequestBody ApplicationVo data, @PathVariable Long applicationId) {
        // 최종제출 되지 않은 지원서 수정 ✅
        return applicationService.updateApplication(data, applicationId);
    }

    @DeleteMapping("/{applicationId}")
    public String deleteApplication(@PathVariable Long applicationId) {
        // 최종제출 되지 않은 지원서 삭제 ✅
        return applicationService.deleteApplication(applicationId);
    }

    @PostMapping("/{applicationId}")
    public String submitApplication(@PathVariable Long applicationId) {
        // 지원서 최종 제출 ✅
        return applicationService.submitApplication(applicationId);
    }

    // 관리자 전용 api
    @GetMapping("/admin/{targetId}")
    public String getAllApplicationStatus(@PathVariable Long targetId, Long memberId) {
        // 관리자의 회원별 모든 지원서 상태확인 ✅
        return applicationService.findAllApplicationByMember(targetId, memberId);
    }

    @PutMapping("/admin/{applicationId}")
    public String updateApplicationStatus(@PathVariable Long applicationId, Long memberId, String status) {
        // 관리자의 지원서 내용 합/불합 상태값 변경 ✅
        return applicationService.updateApplicationStatus(applicationId, memberId, status);
    }
}
