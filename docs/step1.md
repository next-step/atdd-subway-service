# 1단계 - 인수 테스트 기반 리팩터링
## 요구사항
- [ ] LineService 리팩터링
    - [ ] Domain 으로 옮길 로직 찾기
    - [ ] Domain 의 단위 테스트 작성하기
    - [ ] 비즈니스 로직을 Domain 으로 옮기기
- [ ] LineSectionAcceptanceTest 리팩터링
    - [ ]  시나리오, 흐름을 검증하는 테스트로 리팩토링

### 인수조건 예시
```
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 구간 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```