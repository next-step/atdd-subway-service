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

## 요구사항

- LineSectionAcceptanceTest 리팩터링
    - 인수 테스트 통합
        - API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
        - 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
- LineService 리팩터링
    - 인수 테스트 기반 리팩터링
        - LinService의 비즈니스 로직을 도메인으로 옮기기
        - 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
        - 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

    1. Domain으로 옮길 로직을 찾기
    2. Domain의 단위 테스트를 작성하기
    3. 로직을 옮기기

- 토큰 발급 기능 (로그인) 인수 테스트 만들기
    - 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
    - AuthAcceptanceTest 인수 테스트 만들기
    - request 
        ```jsonpath
        POST /login/token HTTP/1.1
        content-type: application/json; charset=UTF-8
        accept: application/json
        {
            "password": "password",
            "email": "email@email.com"
        }
        ```
    - response 
        ```jsonpath
        HTTP/1.1 200
        Content-Type: application/json
        Transfer-Encoding: chunked
        Date: Sun, 27 Dec 2020 04:32:26 GMT
        Keep-Alive: timeout=60
        Connection: keep-alive
        {
            "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
        }
        ```
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기