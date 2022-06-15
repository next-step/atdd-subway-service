### Step3 요구사항

- [ ] 토큰 발급 기능(로그인) (POST /login/token)
    - [ ] 이메일, 패스워드를 받아 인증 후 access token 응답
- [ ] 내 정보 기능
    - [ ] 내 정보 조회 (GET /members/me)
        - [ ] 토큰을 확인해 로그인 정보를 응답
        - [ ] @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용
    - [ ] 내 정보 변경 기능 (PUT /members/me)
    - [ ] 내 정보 삭제 기능 (DELETE /members/me)
- [ ] 즐겨 찾기 기능 구현
    - [ ] 시나리오 기반으로 인수테스트 작성 및 통과 시키기
    - [ ] 즐겨찾기 생성 (POST /favorites)
    - [ ] 즐겨찾기 목록 불러오기 (GET /favorites)
    - [ ] 즐겨찾기 삭제 (DELETE /favorites/:id)

### Step2 요구사항

- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
    - [x] 인수테스트를 바탕으로 Inside Out으로 테스트를 만들며 진행
    - [x] Path 클래스 생성
    - [x] PathFinder 클래스 생성
    - [x] PathService 생성
    - [x] PathController 생성

### Step1 요구사항

- [x] LineService 리팩터링
    - [x] LineService에 있는 비즈니스 로직을 도메인으로 옮긴다
    - [x] 도메인 로직을 생성 시 tdd로 진행한다.
    - [x] 리팩토링 과정에서 인수테스트를 통과 시켜, 로직을 보호한다.
- [x] (선택) LineSectionAcceptanceTest을 시나리오 흐름을 검증하는 방식으로 변경해 본다.

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
