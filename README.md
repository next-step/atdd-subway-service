<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-service">
</p>

<br>

# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 요구사항

### "Step 1"

- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

### "Step 2"

- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

### "Step 3"

- 토큰 발급 기능
    - [x] 토큰 발급(로그인)을 검증하는 인수 테스트(AuthAcceptanceTest) 만들기
    - [ ] Bearer Auth 유효하지 않은 토큰 인수 테스트 추가
    - [ ] 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
- 내 정보 조회 기능
    - [ ] 내 정보 조회하는 인수테스트 만들기
    - [ ] 토큰을 통한 인증으로 로그인 정보를 받아올 수 있는 기능 구현하기
- 즐겨 찾기 기능
    - [ ] 즐겨찾기 기능을 구현하기
    - [ ] 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

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
