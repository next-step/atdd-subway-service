# 요구사항
## 1단계
  - 인수테스트 기반 리팩토링
    - LineService 리팩토링
  - 인수테스트 통합
    - API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩토링

## 2단계
  - 최단 경로 조회 인수 테스트 만들기
  - 최단 경로 조회 기능 구현하기
    - 테스트가 협력 객체의 세부 구현에 의존하는 경우(가짜 협력 객체 사용)
    - 테스트 대상이 협력 객체와 독립적이지 못하고 변경에 영향을 받는 경우(실제 협력 객체 사용)

## 3단계
  - 로그인(토큰 방식) 인수 테스트 만들기
  - 내 정보 조회 기능 완성하기
  - 즐겨 찾기 기능 완성하기

--- 

# 작업로그
## 1단계
### 인수테스트 기반 리팩토링
  - [X] LineService 리팩토링
    - [X] 노선 생성 기능 리팩토링 준비
    - [X] 노선 생성 기능 리팩토링 완료
    - [X] 노선 생성 기능 코드 중복 제거
    - [X] 구간 추가 기능 리팩토링 준비
    - [X] 구간 추가 기능 리팩토링 완료
    - [X] 구간 추가 기능 코드 중복 제거
    - [X] 구간 삭제 기능 리팩토링 준비
    - [X] 구간 삭제 기능 리팩토링 완료
    - [X] 구간 삭제 기능 코드 중복 제거
 

### 인수테스트 통합
  - [X] LineAcceptanceTest 통합
    - [X] 성공 시나리오 통합
    - [X] 실패 시나리오 통합
  - [X] LineSectionAcceptanceTest 통합
    - [X] 성공 시나리오 통합
    - [X] 실패 시나리오 통합

## 2단계
  - [X] 최단 경로 조회 인수 테스트 만들기
  - [X] 최단 경로 조회 기능 구현하기
    - [X] Outside - in 방식 TDD
      - [X] PathControllerTest 작성(Mock : PathService.findShortestPath)
      - [X] PathServiceTest 작성(Mock : PathFindService.findShortestPath)
        - 추가된 도메인 객체 : PathFindResult, PathFindService
      - [X] PathFindServiceTest 작성

## 3단계 
  - [ ] 토큰 발급 인수 테스트 작성 
  - [ ] 내 정보 조회 기능 구현 
  - [ ] 즐겨 찾기 기능 구현
  - [ ] Inside-out 방식으로 TDD 진행
  
---

# 리뷰 반영 
## 1단계 
  - [X] LineResponse 정적팩토리메서드 리팩토링
  - [X] Line 생성을 위한 빌더 작성
  - [X] Sections 내부 콜렉션 필드명 변경
  - [X] Sections.addMergedSection 리팩토링
  - [X] Section 간 비교를 위한 메서드 추가, 객체 비교를 위한 기존 getter 호출 제거

## 2단계 
  - [X] PathService 중복 제거
  - [X] SectionEdge 접근 제어자 수정
  - [X] JGraphPathFindService에서 Repository 의존성 제거
  - [X] PathService에서 타 도메인의 Service를 사용하도록 변경(Repository에 대한 의존성 제거)
  - [X] lines, graph 캐시 도입 
