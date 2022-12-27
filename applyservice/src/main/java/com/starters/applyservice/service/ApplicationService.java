package com.starters.applyservice.service;

import com.starters.applyservice.entity.Application;
import com.starters.applyservice.entity.Lesson;
import com.starters.applyservice.entity.Member;
import com.starters.applyservice.repository.ApplicationRepository;
import com.starters.applyservice.repository.LessonRepository;
import com.starters.applyservice.repository.MemberRepository;
import com.starters.applyservice.dto.ApplicationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationService {

    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final LessonRepository lessonRepository;

    private final String[] APP_STATUS = {"지원 중", "지원완료", "합격", "불합격"};

    private boolean isHaveSubmit(Long memberId) {
        List<Application> apps = applicationRepository.findAllByMemberId(memberId);
        List<Application> generatedApps = apps.stream().filter(a -> a.getStatus().equals(1)).collect(Collectors.toList());
        return generatedApps.size() >= 1;
    }
    private Integer countApplications(Long memberId) {
        List<Application> apps = applicationRepository.findAllByMemberId(memberId);
        List<Application> generatedApps = apps.stream().filter(a -> !a.getStatus().equals(3)).collect(Collectors.toList());
        return generatedApps.size();
    }

    private boolean isAdmin(Long memberId) {
        Optional<Member> memObj = memberRepository.findById(memberId);
        if (memObj.isPresent()) {
            Boolean status = memObj.get().getIsAdmin();
            return status.equals(true);
        }
        return false;
    }

    private Application isValidApplication(Long applicationId) throws RuntimeException {
        Application appObj = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("지원서가 없음"));
        return appObj;
    }

    private Boolean isValidateClass(Long classId) {
        Optional<Lesson> lessonObj = lessonRepository.findById(classId);
        LocalDate now = LocalDate.now();
        LocalDate start = lessonObj.get().getClassStart();
        LocalDate end = lessonObj.get().getClassEnd();
        return now.isAfter(start) && now.isBefore(end);
    }

    private Boolean isValidateMember(Long memberId) {
        Optional<Member> memObj = memberRepository.findById(memberId);
        return memObj.isPresent();
    }

    public List<Application> findAllApplications() {
        log.info(applicationRepository.findAll().toString());
        return applicationRepository.findAll();
    }

    public String createApplication(ApplicationDto data, Long memberId, Long classId) {
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
            app.setStatus(0);
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

    public String updateApplication(ApplicationDto data, Long applicationId) {
        Application app = isValidApplication(applicationId);
        if (app.getStatus().equals(0)) {
            app.setApplyMotiv(data.getApplyMotiv());
            app.setFutureCareer(data.getFutureCareer());
            applicationRepository.save(app);
            return "지원서 수정 성공";
        } else {
            return "수정할 지원서가 없음";
        }
    }

    public String deleteApplication(Long applicationId) {
        Application app = isValidApplication(applicationId);
        if (app.getStatus().equals(0)) {
            applicationRepository.delete(app);
            return "지원서 삭제 성공";
        }
        return "지원서 삭제 실패";
    }

    public String submitApplication(Long applicationId) {
        // TODO: 모집기간이 동일한 수업에는 3개 이상의 지원서를 지원완료 할 수 없습니다.
        // TODO: 교육기간이 동일한 수업에는 3개 이상의 지원서를 지원완료 할 수 없습니다.
        Application app = isValidApplication(applicationId);
        List<Application> applys = applicationRepository.findAllByMemberIdAndLessonId(app.getMember().getId(), app.getLesson().getId());
        for (Application apply: applys) {
            if (apply.getStatus().equals(1)) {
                return "이미 지원완료한 지원서가 있습니다";
            }
        }
        if (app.getStatus().equals(0)) { // 현재 지원중(0)인 지원서가 있으면
            app.setStatus(1);
            applicationRepository.save(app);
            return "지원서 최종제출 완료";
        }
        return "지원서 최종제출 실패";
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

    public String updateApplicationStatus(Long applicationId, Long memberId, Integer appStatus) {
        if (appStatus.equals(1) || appStatus.equals(0)) {
            if (!isAdmin(memberId)) {
                return "현재 유저가 관리자가 아닙니다";
            }
            if (isValidateMember(memberId)) {
                Optional<Application> appObj = applicationRepository.findById(applicationId);
                if(appObj.isPresent()) {
                    appObj.get().setStatus(appStatus);
                    applicationRepository.save(appObj.get());
                } else { return "수정할 지원서가 없습니다"; }
            } else { return "유저가 존재하지 않습니다"; }
        } else { return "지원서의 상태를 합격 또는 불합격으로 설정바랍니다"; }
        return applicationId.toString() + "번 지원서의 상태 수정을 " + APP_STATUS[appStatus] + "으로 처리했습니다";
    }


    public void setApplicationStatus() {
        List<Lesson> lessons = lessonRepository.findAllByRecruitmentEnd(LocalDate.now().minusDays(1).atStartOfDay());
        for (Lesson lesson:lessons) {
            lesson.setStatus(false);
        }
    }

    public List<ApplicationDto> searchApplicationByName(String memberName) {
        List<ApplicationDto> memberApps = applicationRepository.findAllByMemberNameAndStatusIs(memberName, 1).stream().map(app -> new ApplicationDto(app.getId(), app.getMember().getId(), app.getLesson().getId(), app.getApplyMotiv(), app.getFutureCareer(), app.getStatus())).collect(Collectors.toList());
        return memberApps;
    }
}

