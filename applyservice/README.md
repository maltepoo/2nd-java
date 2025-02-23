# API 명세서
### 회원
- (GET) 관리자/ 회원별 모든 지원서의 상태를 확인한다(/api/application/admin/{targetId})
  - targetId는 원하는 회원의 id를 받음
- (POST) 관리자/ 지원서 내용 합/불합 상태값을 변경한다(/api/application/admin/{applicationId})
  - 상태값을 변경할 application id를 Path variable로, memberId, 변경할 상태값을 파라미터로 받는다

### 지원
- (GET) 모든 유저의 모든 지원서를 조회한다 (/application)
- (POST) 지원서를 작성한다 (/application)
  - body로 지원 포부와, 미래계획을 받음
  - memberId와 classId를 받음
- (GET) 특정 지원서를 조회한다 (/application/{applicationId})
- (PUT) 최종제출 되지 않은 지원서를 수정한다 (/application/{applicationId})
  - body로 수정할 데이터와 지원서 Id값을 받음
- (DELETE) 최종제출 되지 않은 지원서를 삭제한다 (/application/{applicationId})
- (POST) 최종제출 되지 않은 지원서를 최종제출한다 (/application/{applicationId})

- (GET) (관리자) 원하는 회원의 모든 지원서 상태를 확인한다 (/application/admin/{targetId})
- (PUT) (관리자) 관리자의 지원서 내용 합/불합 상태값 변경 (/application/admin/{targetId})
- (GET) (관리자) 완료된 지원서는 회원이름으로 검색 (/application/admin/search?={memberName})


# 기능구현
- 공통 (+20/-20), 부분점수, 각 문항 당
- [x] Class Diagram 작성
- [x] E-R Diagram 작성
- [x] API 명세서 작성

- ### 일반회원
- [x] (+20/-20) 회원은 최대 5개의 지원서를 작성할 수 있습니다.
- [x] (+20/-10) 수업 별 중복 지원서가 존재할 수 있으며, 최종 지원완료 지원서는 1개만 존재할 수 있습니다.
- [x] (+20/-10) 수업의 모집기간 내에만 지원서를 작성할 수 있으며, 모집기간 전후로 지원서 작성은 불가합니다.
- [x] (+20/-20) 지원서는 지원 중, 지원완료, 합격, 불합격 총 4가지로 상태값을 관리합니다.
- [x] (+20/-10) 지원 중일때는 임시저장 상태로 일반회원이 지원완료 전까지 수정이 가능합니다.
- [x] (+20/-10) 지원완료 상태는 지원서 제출 이후 수정, 삭제가 불가능 합니다.
- [x] (+20/-20) 모집기간이 종료될 경우 지원 중의 지원서는 불합격으로 변경됩니다.
- [x] (+20/-10) 지원서 상태값이 불합격으로 변경된 경우 지원서 개수로 카운팅 하지 않습니다.
- [x] (선택) 모집기간이 동일한 수업에는 3개 이상의 지원서를 지원완료 할 수 없습니다.
- [x] (선택) 교육기간이 동일한 수업에는 3개 이상의 지원서를 지원완료 할 수 없습니다.

- ### 관리자
- [x] (+20/-10) 관리자 회원은 각 회원 별 지원서의 상태를 확인할 수 있습니다. (지원 중, 지원완료)
- [x] (+20/-10) 지원완료된 지원서의 경우 지원서 내용 및 합격/불합격 상태값 변경이 가능합니다.
- [x] (선택) 완료된 지원서는 회원이름으로 검색이 가능합니다.