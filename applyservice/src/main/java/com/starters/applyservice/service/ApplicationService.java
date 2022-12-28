package com.starters.applyservice.service;

import com.starters.applyservice.dto.RequestApplicationDto;
import com.starters.applyservice.entity.*;
import com.starters.applyservice.repository.ApplicationRepository;
import com.starters.applyservice.repository.LessonRepository;
import com.starters.applyservice.repository.MemberRepository;
import com.starters.applyservice.dto.ApplicationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String createApplication(RequestApplicationDto data, Long memberId, Long classId) {
        if (countApplications(memberId, classId)) {
            return "수업에 지원서는 최대 5개까지 작성 가능합니다";
        }
        if (isHaveSubmit(memberId, classId)) {
            return "해당 수업에 이미 지원완료한 지원서가 있습니다";
        }
        if (isValidateClass(classId)) {
            Application app = new Application();
            Lesson lesson = lessonRepository.findById(classId).orElseThrow(() -> new IllegalArgumentException("수업이 없음"));
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("멤버가 없음"));

            app.setLesson(lesson);
            app.setMember(member);

            if (!data.getApplyMotiv().isEmpty()) {
                app.setApplyMotiv(data.getApplyMotiv());
            } else { return "지원동기를 입력해주세요"; }
            if (!data.getFutureCareer().isEmpty()) {
                app.setFutureCareer(data.getFutureCareer());
            } else { return "포부를 입력해주세요"; }

            app.setStatus(ApplyStatus.APPLYING);

            applicationRepository.save(app);
        } else {
            return "모집종료된 지원서입니다.";
        }
        return "지원서가 작성 완료되었습니다. 기간 내에 작성완료를 눌러 최종제출 해주세요.";
    }

    public String findApplication(Long applicationId) {
        Optional<Application> app = applicationRepository.findById(applicationId);
        if (app.isPresent()) {
            return "지원서 단건조회 성공" + app;
        }
        return "지원서 단건조회 실패";
    }

    public String updateApplication(RequestApplicationDto data, Long applicationId) {
        Application app = isValidApplication(applicationId);
        if (app.getStatus().equals(ApplyStatus.APPLYING)) {
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
        if (app.getStatus() == ApplyStatus.APPLYING) {
            applicationRepository.delete(app);
            return "지원서 삭제 성공";
        }
        return "지원서 삭제 실패";
    }

    public String submitApplication(Long applicationId) {
        Application app = isValidApplication(applicationId);

        // 제출하는 현재 지원서가 지원중인지 확인
        if (app.getStatus() == ApplyStatus.APPLYING) {
            //현재 제출하는 수업에 최종제출 한 지원서가 있는지 확인
            List<Application> submittedApply = applicationRepository.findAllByMemberIdAndLessonId(app.getMember().getId(), app.getLesson().getId()).stream().filter(a -> a.getStatus().equals(ApplyStatus.SUBMITTED)).collect(Collectors.toList());
            if (submittedApply.size() > 1) {
                return "이미 최종제출한 지원서가 있습니다";
            }
            // 모집기간이 동일한 수업에는 3개 이상의 지원서를 지원완료 했는지 확인
            if (isMultipleApplicationSubmittedForSameRecruitmentPeriod(app)) {
                return "이미 모집기간이 동일한 3개의 수업에 지원했습니다";
            }
            // 교육기간이 동일한 수업에는 3개 이상의 지원서를 지원완료
            if (isMultipleApplicationSubmittedForSameTeachingPeriod(app)) {
                return "이미 수업기간이 동일한 3개의 수업에 지원했습니다";
            }

            app.setStatus(ApplyStatus.SUBMITTED);
            applicationRepository.save(app);
            return "지원서 최종제출 완료";
        }
        return "지원서 최종제출 실패";
    }

    private boolean isHaveSubmit(Long memberId, Long classId) {
        // 지원하는 수업에 이미 제출완료된 지원서가 있는지 확인
        List<Application> applys = applicationRepository.findAllByMemberIdAndLessonId(memberId, classId).stream().filter(a -> a.getStatus() == ApplyStatus.SUBMITTED ).collect(Collectors.toList());
        return applys.size() >= 1;
    }
    private boolean countApplications(Long memberId, Long classId) {
        List<Application> applys = applicationRepository.findAllByMemberIdAndLessonId(memberId, classId);
        return applys.size() >= 5;
    }

    private boolean isAdmin(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found member having received memberId"));
        return member.getIsAdmin() == Role.ADMIN;
    }

    private Application isValidApplication(Long applicationId) throws RuntimeException {
        Application apply = applicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("지원서가 없음"));
        return apply;
    }

    private Boolean isValidateClass(Long classId) {
        Lesson lesson = lessonRepository.findById(classId).orElseThrow(() -> new RuntimeException("지원할 수업이 없습니다."));
        return LocalDate.now().isBefore(lesson.getRecruitmentEnd()) || LocalDate.now().isEqual(lesson.getRecruitmentEnd());
    }

    private Boolean isValidateMember(Long memberId) {
        Optional<Member> memObj = memberRepository.findById(memberId);
        return memObj.isPresent();
    }

    public List<Application> findAllApplications() {
        log.info(applicationRepository.findAll().toString());
        return applicationRepository.findAll();
    }

    private Boolean isMultipleApplicationSubmittedForSameRecruitmentPeriod(Application apply) {
        List<Lesson> lessons = lessonRepository.findAllByRecruitmentStartAndRecruitmentEnd(apply.getLesson().getRecruitmentStart(), apply.getLesson().getRecruitmentEnd());
        return lessons.size() > 3;
    }
    private Boolean isMultipleApplicationSubmittedForSameTeachingPeriod(Application apply) {
        List<Lesson> lessons = lessonRepository.findAllByClassStartAndClassEnd(apply.getLesson().getClassStart(), apply.getLesson().getClassEnd());
        return lessons.size() > 3;
    }


    /**
     * 관리자 전용 api
     **/

    public List<ApplicationDto> findAllApplicationByMember(Long targetId, Long memberId) {
        isAdmin(memberId);
        isValidateMember(targetId);
        return applicationRepository.findAllByMemberId(targetId).stream().map(app -> new ApplicationDto(app.getId(), app.getMember().getId(), app.getLesson().getId(), app.getApplyMotiv(), app.getFutureCareer(), app.getStatus())).collect(Collectors.toList());
    }

    public String updateApplicationStatus(Long applicationId, Long adminId, String appStatus) {
        Application app = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException("해당 지원서가 없음"));
        Member admin = memberRepository.findById(adminId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없음"));
        if (admin.getIsAdmin().equals(Role.ADMIN)) { // 관리자인지 확인
            if (app.getStatus().equals(ApplyStatus.SUBMITTED)) { // 수정하는 지원서가 제출되었는지 확인
                app.setStatus(ApplyStatus.valueOf(appStatus));
            }
        }
        return applicationId + "번 지원서의 상태 수정을 " + appStatus + "으로 처리했습니다";
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

