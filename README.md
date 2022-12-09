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

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

- LineService 비즈니스 로직 도메인으로 옮기기
  - [X] saveLine
    - 정적 메소드 from 사용하기
    - Sections 일급컬렉션 사용하여 비즈니스 로직 옮기기
    - Distance 값 타입 변경
  - [X] findLines
  - [X] findLineById
  - [X] findLineResponseById
  - [X] updateLine
  - [X] addLineStation
    - 비즈니스 로직 도메인으로 이동
  - [X] removeLineStation
    - 비즈니스 로직 도메인으로 이동

- LineSectionAcceptanceTest 리팩터링
  - [X] 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
    - dynamicTest 활용하여 시나리오 검증

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

## 2단계 - 경로 조회 기능

### 요구사항

- [X] 최단 경로 조회 인수 테스트 만들기
  - PathAcceptanceTest 생성
- [X] 최단 경로 조회 기능 구현하기
  - Path 도메인 관련 기능 구현하기

```
Feature: 지하철 경로 조회
  Background
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
  Scenario: 출발역과 도착역 사이 최단 경로 조회
    When 지하철 경로 조회 요청
    Then 최단 경로 조회됨
    When 출발역과 도착역이 연결 안된 경우
    Then 경로 조회 실패됨
    When 출발역과 도착역이 같은 경우
    Then 경로 조회 실패됨
```

## 3단계 - 인증을 통한 기능 구현

### 요구사항

- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기

```
Feature: 로그인 성공
  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
    
Feature: 로그인 실패
  Scenario: 로그인을 시도한다.
    Given 회원이 등록되어 있지 않음
    When 로그인 요청
    Then 로그인 실패
```

- [X] 인증 - 내 정보 조회 기능 완성하기

```
Feature: 나의 정보 관련
  Scenario: 로그인을 성공하고 나의 정보를 조회합니다
    Given 새로운 회원을 등록한다
    When 로그인하지 않음
    Then 나의 정보를 조회할 수 없음
    When 로그인을 통해 생성된 토큰을 이용하여 나의 정보 조회 요청
    Then 나의 정보가 조회됨
    When 로그인을 통해 생성된 토큰을 이용하여 비밀번호 수정 요청
    Then 패스워드가 수정됨
    When 변경된 패스워드로 로그인 요청
    Then 로그인 성공
    When 로그인을 통해 생성된 토큰을 이용하여 회원 탈퇴 요청
    Then 회원 정보가 삭제됨
```

- [ ] 인증 - 즐겨 찾기 기능 완성하기


<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
