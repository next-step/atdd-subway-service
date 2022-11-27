<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.

---

# 인증 테스트 기반 TDD

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

```text
- LineService 리팩터링
- (선택) LineSectionAcceptanceTest 리팩터링
```

#### LineService 리팩토링

1. Domain으로 옮길 로직을 찾기 (대상 : LineService)

- [x] saveLine
- [x] findLines
- [x] findLineResponseById
- [x] updateLine
- [x] addLineStation
- [x] removeLineStation

2. Domain의 단위 테스트를 작성하기

- 서비스 레이어에서 옮겨 올 로직의 기능을 테스트

3. 로직을 옮기기

- 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- 정상 동작 확인 후 기존 로직 제거

#### (선택) LineSectionAcceptanceTest 리팩터링

```text
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

```text
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 구간 순서 없이 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

```text
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 이미 존재하는 지하철 구간 등록 요청
    Then 지하철 구간 등록 실패됨
```

```text
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 노선에 존재하지 않는 지하철역으로 지하철 구간 등록 요청
    Then 지하철 구간 등록 실패됨
```

```text
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 노선에 지하철역이 두개일 때 지하철역 제외 요청
    Then 지하철 구간 등록 실패됨
```

## 2단계 - 경로 조회 기능

### 요구사항

1. 최단 경로 조회 인수 테스트 만들기

```text
Feature: 지하철 경로 관련 기능

  Background
    Given 지하철역 여러개 등록되어 있음
    And 지하철 노선 여러개 등록되어 있음
    And 지하철 노선에 지하철역(지하철 구간) 여러개 등록되어 있음
   
  Scenario: 출발역과 도착역 사이의 최단 경로 조회
    When 지하철 경로 조회 요청
    Then 출발역과 도착역 사이의 최단 경로 조회됨.
    
  Scenario: 출발역과 도착역이 같은 경우 최단 경로 조회
    When 지하철 경로 조회 요청
    Then 최단 경로 조회 실패
    
  Scenario: 출발역과 도착역이 연결이 되어 있지 않은 경우 최단 경로 조회
    When 지하철 경로 조회 요청
    Then 최단 경로 조회 실패
    
  Scenario: 존재하지 않은 출발역 또는 도착역으로 최단 경로 조회
    When 지하철 경로 조회 요청
    Then 최단 경로 조회 실패
```

2. 최단 경로 조회 기능 구현하기

- Outside In
    - 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
    - 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
    - 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
    - Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
- Inside Out
    - 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
    - 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
    - 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작

---

## 3단계 - 인증을 통한 기능 구현

### 요구사항

1. 토근 발급 기능(로그인) 인수 테스트 만들기

- 인수 조건
  ```text
  Feature: 로그인 기능
  
    Scenario: 로그인을 시도한다.
      Given 회원 등록되어 있음
      When 로그인 요청
      Then 로그인 됨
  ```
- 예외 케이스도 고려하여 구현하기
- 유효하지 않은 토큰으로 `/members/me` 요청을 보낼 경우에 대한 예외 처리

2. 인증 - 내 정보 조회 기능 완성하기

- 인수 조건
  ```text
  Feature: 나의 정보 관리 기능
  
    Background
      Given 회원 등록되어 있음
      And 로그인 되어있음
  
    Scenario: 나의 정보를 관리(조회/수정/삭제)한다.
      When 내 정보 조회 요청
      Then 내 정보 조회 됨
      When 내 정보 수정 요청
      Then 내 정보 수정 됨
      When 내 정보 삭제 요청
      Then 내 정보 삭제 됨
  ```
- `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용하기
- `/members/me` API (GET|PUT|DELETE) 동작 확인

3. 인증 - 즐겨 찾기 기능 완성하기

- 인수 조건
  ```text
  Feature: 즐겨찾기를 관리한다.
  
    Background 
      Given 지하철역 등록되어 있음
      And 지하철 노선 등록되어 있음
      And 지하철 노선에 지하철역 등록되어 있음
      And 회원 등록되어 있음
      And 로그인 되어있음
  
    Scenario: 즐겨찾기를 관리
      When 즐겨찾기 생성을 요청
      Then 즐겨찾기 생성됨
      When 즐겨찾기 목록 조회 요청
      Then 즐겨찾기 목록 조회됨
      When 즐겨찾기 삭제 요청
      Then 즐겨찾기 삭제됨
  ```
