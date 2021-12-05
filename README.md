

# 🚀 1단계 - 인수 테스트 기반 리팩터링

## 요구사항
- [ ] LineSectionAcceptanceTest 리팩터링
- [ ] LineService 리팩터링

### 리팩토링 순서 

### LineService 메소드 정리 
1. 지하철 노선을 저장한다.
2. 지하철 전체 목록을 조회한다. 
3. 지하철 노선 하나를 조회한다. 
4. 지하철 노선을 수정한다. 
5. 지하철 노선을 삭제한다.
6. 지하철 섹션을 추가한다.
7. 지하철 섹션을 삭제한다. 
8. 지하철 역을 조회한다. 
9. 첫번째 역을 조회한다. 

### 영향도 체크 

1. 조회 기능
   - 수정대상 
     - findUpStation
     - getStations
   - 사용범위 
     - saveLine 
     - findLines
     - findLineResponseById 
     - addSection

2. 세션 구간 추가 
   - 수정대상 
     - addLineStation
   - 사용범위 
     - lineService
3. 삭제 
   - 수정대상 
     - removeLineStation
   - 사용범위 
     - lineService


### 인수테스트 시나리오
Feature: 지하철 구간 관련 기능


Scenario: 지하철 구간을 관리
    When 지하철 구간 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨



# 2단계 - 경로 조회 기능

## 요구사항 

- [ ] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

### 응답 포맷 
```http request
   http://localhost:55494/paths?source=1&target=6
```

### 요구사항 예외 사항 부분 
예외 상황 예시
- [ ] 출발역과 도착역이 같은 경우
- [ ] 출발역과 도착역이 연결이 되어 있지 않은 경우
- [ ] 존재하지 않은 출발역이나 도착역을 조회 할 경우
