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

## 🚀 미션 진행 상황

### 1단계 - 인수 테스트 기반 리팩터링

#### 기능 요구사항

* [x] LineService 리팩터링
* [x] (선택) LineSectionAcceptanceTest 리팩터링

### 2단계 - 경로 조회 기능

#### 기능 요구사항

* [x] 최단 경로 조회 인수 테스트 만들기
* [x] 최단 경로 조회 기능 구현하기

### 3단계 - 인증을 통한 기능 구현

#### 기능 요구사항

* [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
* [ ] 인증 - 내 정보 조회 기능 완성하기
  * 내 정보 조회 시나리오
    ```markdown
      인수테스트 시나리오
        Feature: 내 정보 관련 기능
          Background
            Given 계정을 등록한다.
          Scenario: 내정보를 관리
            When 로그인 한다.
            Then 토큰 확인됨.
            When 내 정보를 조회한다.
            Then 정보 조회됨.
            When 내 정보를 수정한다.
            Then 정보 수정됨.
            When 내 계정을 탈퇴한다.
            Then 계정 탈퇴됨.
            When 탈퇴한 계정 정보로 로그인 한다.
            Then 로그인 실패함.
    ```
* [ ] 인증 - 즐겨 찾기 기능 완성하기

## ✏️ Code Review Process

[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-service/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-service/blob/master/LICENSE.md) licensed.
