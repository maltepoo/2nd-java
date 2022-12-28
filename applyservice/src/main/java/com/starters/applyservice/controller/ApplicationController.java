package com.starters.applyservice.controller;

import com.starters.applyservice.dto.RequestApplicationDto;
import com.starters.applyservice.dto.ResponseDto;
import com.starters.applyservice.service.ApplicationService;
import com.starters.applyservice.dto.ApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("")
    public String getAllApplications() {
        // 모든 지원서를 조회
        return applicationService.findAllApplications().toString();
    }

    @PostMapping("")
    public String application(@RequestBody RequestApplicationDto data, @RequestParam Long memberId, @RequestParam Long classId) {
        // 클래스에 해당하는 지원서를 작성
        System.out.println("data = " + data);
        return applicationService.createApplication(data, memberId, classId);
    }

    @GetMapping("/{applicationId}")
    public String getApplication(@PathVariable Long applicationId) {
        // 특정 지원서 조회
        return applicationService.findApplication(applicationId);
    }

    @PutMapping("/{applicationId}")
    public String updateApplication(@RequestBody RequestApplicationDto data, @PathVariable Long applicationId) {
        // 최종제출 되지 않은 지원서 수정
        return applicationService.updateApplication(data, applicationId);
    }

    @DeleteMapping("/{applicationId}")
    public String deleteApplication(@PathVariable Long applicationId) {
        // 최종제출 되지 않은 지원서 삭제
        return applicationService.deleteApplication(applicationId);
    }

    @PostMapping("/{applicationId}")
    public String submitApplication(@PathVariable Long applicationId) {
        // 지원서 최종 제출
        return applicationService.submitApplication(applicationId);
    }

    /**
     * 관리자 전용 API
     * */
    @GetMapping("/admin/{targetId}")
    public ResponseDto getAllApplicationStatus(@PathVariable Long targetId, @RequestParam Long memberId) {
        // 관리자의 회원별 모든 지원서 상태확인
        List<ApplicationDto> res = applicationService.findAllApplicationByMember(targetId, memberId);
        return new ResponseDto<>(HttpStatus.OK, "모든 지원서 가져오기 성공", res);
    }

    @PutMapping("/admin/{applicationId}")
    public String updateApplicationStatus(@PathVariable Long applicationId, @RequestParam Long memberId, @RequestParam String status) {
        // 관리자의 지원서 내용 합/불합 상태값 변경
        return applicationService.updateApplicationStatus(applicationId, memberId, status);
    }

    @GetMapping("/admin/search")
    public ResponseDto searchApplicationByName(@RequestParam String memberName) {
        // 완료된 지원서는 회원이름으로 검색이 가능
        List<ApplicationDto> res = applicationService.searchApplicationByName(memberName);
        return new ResponseDto<>(HttpStatus.OK, "지원 완료된 지원서 검색성공", res);
    }
}
