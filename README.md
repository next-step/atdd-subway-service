# 인수테스트 기반 TDD

## Step1. 인수테스트 기반 리팩터링

### 요구사항

- [x] LineService 리팩터링 
  - [x] 도메인으로 옮길 로직을 찾기
  - [x] 도메인의 단위 테스트를 작성하기
    - [x] 도메인 단위테스트 목록 작성하기
    - [x] SectionsTest 단위테스트 작성하기
    - [x] LineTest 단위테스트 작성하기
  - [x] 로직을 옮기기
    - [x] 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
    - [x] 정상 동작 확인 후 기존 로직 제거
- [x] LineSectionAcceptanceTest 리팩터링
  - [x] 통합테스트 생성

```
인수 조건 예시

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

## Step2. 경로 조회 기능

### 요구사항

- [x] 최단 경로 조회 인수 테스트 만들기
  - [x] 성공 케이스
    - [x] 최단거리, 최단 경로 검증
  - [x] 실패 케이스
    - [x] 출발역과 도착역이 같은 경우
    - [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
    - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우
- [x] 최단 경로 조회 기능 구현하기
  - [x] 다양한 경로조회 전략이 가능하도록 외부에서 의존성 주입

## Step2. 경로 조회 기능

### 요구사항

- [x] 최단 경로 조회 인수 테스트 만들기
  - [x] 성공 케이스
    - [x] 최단거리, 최단 경로 검증
  - [x] 실패 케이스
    - [x] 출발역과 도착역이 같은 경우
    - [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
    - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우
- [x] 최단 경로 조회 기능 구현하기
  - [x] 다양한 경로조회 전략이 가능하도록 외부에서 의존성 주입

## Step3. 인증을 통한 기능 구현

### 요구사항

-[x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  - [x] 인증관련 인수테스트 작성
  - [x] 인증관련 서비스테스트 작성
  - [x] 인증관련 기능 구현
-[x] 인증 - 내 정보 조회 기능 완성하기
  - [x] 내정보 조회 통합테스트 작성
  - [x] 내정보 관리 기능 생성
-[x] 인증 - 즐겨 찾기 기능 완성하기
  - [x] 즐겨찾기 통합테스트 작성
  - [x] 즐겨찾기 컨트롤러 기능 구현
  - [x] 즐겨찾기 서비스테스트 생성(Mock)
  - [x] 즐겨찾기 서비스 기능 구현
  - [x] 즐겨찾기 도메인 테스트 작성
  - [x] 즐겨찾기 도메인 기능 구현

## Step4. 요금 조회

### 요구사항

- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
  - [x] 인수조건 수정 - 경로조회시 요금 검증 추가
  - [x] 요금 객체 추가 - Fare
  - [x] 경로 도메인 및 응답객체에 요금 항목 추가
  - [x] 경로 생성자에 요금 계산 기능 추가
- [x] 노선별 추가 요금 정책 추가
  - [x] 노선에 추가요금 항목 추가 
  - [x] 역 그래프에서 노선 찾기위해 SectionEdge 클래스 추가
- [x] 연령별 할인 정책 추가
  - [x] 할인 enum 객체 생성
  - [x] 할인 동작을 위한 LoginMember 수정
  - [x] 연령별 할인이 되도록 하기 위해 경로조회 기능을 게스트 경로조회/ 유저 경로조회로 구분