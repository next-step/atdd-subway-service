

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

- [x] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

### 응답 포맷 
```http request
   http://localhost:55494/paths?source=1&target=6
```

### 요구사항 예외 사항 부분 
예외 상황 예시
출발역과 도착역이 같은 경우
출발역과 도착역이 연결이 되어 있지 않은 경우
존재하지 않은 출발역이나 도착역을 조회 할 경우


### 시나리오 작성 
```integrationperformancetest
  Feature : 지하철 경로 관리 
  Background : 지하철 역 등록 및 지하철역 라인등록
  
  Scenario : 최단 경로 조회
      when 지하철 경로를 조회 
      then 지하철 경로 응답됨
      then 가장_빠른_지하철노선_조회
```


### 기능명세서
- 시작역부터 종료역까지의 거리를 생각해보자


- [x] 지하철 탐색을 위한 라이브러리용 컬렉션 생성 
  - [x] 데이터를 생성
    - [x] 노선이 Null이면 예외 
  - [x] 데이터를 조회
- [x] 지하철 탐색시 가중치에 따른 가장 빠른노선을 조회한다. 
- [x] 출발역과 도착역이 같은 경우
- [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
- [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우


# 3단계 인증을 통한 기능 구현 

## 요구사항
- [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] `인증` - 내 정보 조회 기능 완성하기 
- [ ] `인증` - 즐겨 찾기 기능 완성하기

## 요구사항 설명
- [ ] AuthAcceptanceTest 인수테스트 작성하기
  - [ ] 이메일과 패스워드 통해 로그인
- [ ] Bearer Auth 유효하지 않은 토큰 인수 테스트
- [ ] 유효하지 않은 토큰으로 `/members/me` 요청을 보낼 경우에 대한 예외 처리
- [ ] `/members/me` 로 요청시 기능 수행
  - [ ] 로그인 후 발급 받은 토큰을 포함해서 요청 하기
  - [ ] 내 정보 조회 
  - [ ] 내 정보 수정 
  - [ ] 내 정보 삭제

## 시나리오 

### 로그인 
```integrationperformancetest
  Feature 로그인 기능 
  Background  
    given 회원이 등록되어있음

  Scenario 회원정보가_있는경우_토큰_발급
      when 로그인_요청
      
      then 로그인 됨 
      then 토큰 생성됨

  Scenario 유효하지 않는 토큰인 경우 실패
      when 로그인_요청 
      then 토큰 생성 실패
```  

### 로그인 후 시나리오 변경 
```integrationperformancetest

  Scenario  내 정보 조회하기
      then  토큰_생성됨
      
      when  내 정보 조회하기
      then  내 정보 조회됨
      
   Scenario  내 정보 수정하기
      then  로그인 됨
      when  내 정보 조회하기
      then  로그인 정보 변경됨
      
   Scenario 내 정보 삭제하기
      then  로그인 됨
      when  내 정보 삭제하기 
      then  로그인 삭제됨
      when  로그인 요청
      then  토큰 생성 실패
```


### 즐겨찾기 
```integrationperformancetest
  Feature 즐겨찾기를 관리한다.

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음

  Scenario 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨
```


