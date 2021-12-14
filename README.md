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

# 1단계 - 인수 테스트 기반 리팩터링

인수 테스트 기반 리팩터링

- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

1. Domain으로 옮길 로직을 찾기

- 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
- 객체지향 생활체조를 참고

2. Domain의 단위 테스트를 작성하기

- 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
- SectionsTest나 LineTest 클래스가 생성될 수 있음

3. 로직을 옮기기

- 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- 정상 동작 확인 후 기존 로직 제거

### 요구사항

- [X] LineSectionAcceptanceTest 리팩터링
- [X] LineService 리팩터링

# 2단계 - 경로 조회 기능
Request

```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

Response
```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "stations": [
        {
            "id": 5,
            "name": "양재시민의숲역",
            "createdAt": "2020-05-09T23:54:12.007"
        },
        {
            "id": 4,
            "name": "양재역",
            "createdAt": "2020-05-09T23:54:11.995"
        },
        {
            "id": 1,
            "name": "강남역",
            "createdAt": "2020-05-09T23:54:11.855"
        },
        {
            "id": 2,
            "name": "역삼역",
            "createdAt": "2020-05-09T23:54:11.876"
        },
        {
            "id": 3,
            "name": "선릉역",
            "createdAt": "2020-05-09T23:54:11.893"
        }
    ],
    "distance": 40
}
```

### 요구사항

- [X] 최단 경로 조회 인수 테스트 만들기
  - [X] 최단 경로 조회
  - [X] 최단 경로 조회 예외 - 출발역과 도착역이 같은 경우
  - [X] 최단 경로 조회 예외 - 출발역과 도착역이 연결이 되어 있지 않은 경우
- [X] 최단 경로 조회 기능 구현하기
  - [X] 최단 경로 조회
  - [X] 최단 경로 조회 예외 - 출발역과 도착역이 같은 경우
  - [X] 최단 경로 조회 예외 - 출발역과 도착역이 연결이 되어 있지 않은 경우

# 3단계 - 인증을 통한 기능 구현

- 토큰 발급 인수 테스트
  - 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
  - AuthAcceptanceTest 인수 테스트 만들기

- 인수 조건
```
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

- request
```
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

- response
```
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

### 요구사항
- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] 인증 - 내 정보 조회 기능 완성하기
- [ ] 인증 - 즐겨 찾기 기능 완성하기
