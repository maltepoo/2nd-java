package com.starters.applyservice.service;

import com.starters.applyservice.domain.Application;
import com.starters.applyservice.domain.Lesson;
import com.starters.applyservice.domain.Member;
import com.starters.applyservice.repository.ApplicationRepository;
import com.starters.applyservice.repository.LessonRepository;
import com.starters.applyservice.repository.MemberRepository;
import com.starters.applyservice.vo.ApplicationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationService {

    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, MemberRepository memberRepository, LessonRepository lessonRepository) {
        this.memberRepository = memberRepository;
        this.applicationRepository = applicationRepository;
        this.lessonRepository = lessonRepository;
    }

    private boolean isHaveSubmit(Long memberId) {
        List<Application> apps = applicationRepository.findAllByMemberId(memberId);
        List<Application> generatedApps = apps.stream().filter(a -> a.getStatus().equals("지원완료")).collect(Collectors.toList());
        if (generatedApps.size() >= 1) {
            return true;
        }
        return false;
    }
    private Integer countApplications(Long memberId) {
        List<Application> apps = applicationRepository.findAllByMemberId(memberId);
        List<Application> generatedApps = apps.stream().filter(a -> !a.getStatus().equals("불합격")).collect(Collectors.toList());
        return generatedApps.size();
    }
    private boolean isAdmin(Long memberId) {
        Optional<Member> memObj = memberRepository.findById(memberId);
        if (memObj.isPresent()) {
            Boolean status = memObj.get().getIsAdmin();
            if (status.equals(true)) {
                return true;
            } else { return false; }
        }
        return false;
    }
    private boolean isValidApplication(Long applicationId) {
        Optional<Application> appObj = applicationRepository.findById(applicationId);
        String appStatus = appObj.get().getStatus();
        if (appStatus.equals("지원 중")) {
            return true;
        }
        return false;
    }

    private Boolean isValidateClass(Long classId) {
        Optional<Lesson> lessonObj = lessonRepository.findById(classId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = lessonObj.get().getClassStart();
        LocalDateTime end = lessonObj.get().getClassEnd();

        if (now.isAfter(start) && now.isBefore(end)) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isValidateMember(Long memberId) {
        Optional<Member> memObj = memberRepository.findById(memberId);
        if (memObj.isPresent()) {
            return true;
        }
        return false;
    }

    public List<Application> findAllApplications() {
        log.info(applicationRepository.findAll().toString());
        return applicationRepository.findAll();
    }

    public String createApplication(ApplicationVo data, Long memberId, Long classId) {
        if (countApplications(memberId) >= 5) {
            return "지원서는 최대 5개까지 작성 가능합니다";
        }
        if (isHaveSubmit(memberId)) {
            return "이미 지원완료한 지원서가 있습니다";
        }
        if (isValidateClass(classId)) {
            Application app = new Application();
            app.setLesson(lessonRepository.findById(classId).get());
            if (isValidateMember(memberId)) {
                app.setMember(memberRepository.findById(memberId).get());
            } else { return "해당 지원자가 없음"; }
            if (!data.getApplyMotiv().isEmpty()) {
                app.setApplyMotiv(data.getApplyMotiv());
            } else { return "지원동기를 입력해주세요"; }
            if (!data.getFutureCareer().isEmpty()) {
                app.setFutureCareer(data.getFutureCareer());
            } else { return "포부를 입력해주세요"; }
            app.setStatus("지원 중");
            applicationRepository.save(app);
        } else {
            return "모집종료된 지원서입니다";
        }
        return "지원서가 작성 완료되었습니다. 기간 내에 작성완료를 눌러 최종제출 해주세요.";
    }

    public String findApplication(Long applicationId) {
        Optional<Application> app = applicationRepository.findById(applicationId);
        if (app.isPresent()) {
            return "지원서 단건조회 성공" + app.toString();
        }
        return "지원서 단건조회 실패";
    }

    public String updateApplication(ApplicationVo data, Long applicationId) {
        Application app = applicationRepository.findById(applicationId).get();
        if (isValidApplication(applicationId)) {
            app.setApplyMotiv(data.getApplyMotiv());
            app.setFutureCareer(data.getFutureCareer());
            applicationRepository.save(app);
            return "지원서 수정 성공";
        } else {
            return "수정할 지원서가 없음";
        }
    }

    public String deleteApplication(Long applicationId) {
        if (isValidApplication(applicationId)) {
            Optional<Application> app = applicationRepository.findById(applicationId);
            if (app.isPresent()) {
                applicationRepository.delete(app.get());
                return "지원서 삭제 성공";
            }
            return "지원서 삭제 실패";
        } return "삭제할 지원서가 없음";
    }

    public String submitApplication(Long applicationId) {
        if (isValidApplication(applicationId)) {
            Optional<Application> app = applicationRepository.findById(applicationId);
            if (app.isPresent()) {
                app.get().setStatus("지원완료");
                applicationRepository.save(app.get());
                return "지원서 최종제출 완료";
            }
            return "지원서 최종제출 실패";
        } return "지원서 최종제출 실패";
    }


    /**
     * 관리자 전용 api
     **/

    public String findAllApplicationByMember(Long targetId, Long memberId) {
        if (!isValidateMember(memberId)) {
            return "유저가 존재하지 않습니다";
        }
        if (!isValidateMember(targetId)) {
            return "조회할 유저가 존재하지 않습니다";
        }
        if (!isAdmin(memberId)) {
            return "현재 유저가 관리자가 아닙니다";
        }
        return applicationRepository.findAllByMemberId(targetId).toString();
    }

    public String updateApplicationStatus(Long applicationId, Long memberId, String status) {
        if (status.equals("합격") || status.equals("불합격")) {
            if (!isAdmin(memberId)) {
                return "현재 유저가 관리자가 아닙니다";
            }
            if (isValidateMember(memberId)) {
                Optional<Application> appObj = applicationRepository.findById(applicationId);
                if(appObj.isPresent()) {
                    appObj.get().setStatus(status);
                    applicationRepository.save(appObj.get());원
                } else { return "수정할 지원서가 없습니다"; }
            } else { return "유저가 존재하지 않습니다"; }
        } else { return "지원서의 상태를 합격 또는 불합격으로 설정바랍니다"; }
        return applicationId.toString() + "번 지원서의 상태 수정을 " + status.toString() + "으로 처리했습니다";
    }



}

