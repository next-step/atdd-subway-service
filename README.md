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


## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항

- LineSectionAcceptanceTest 리팩터링
- LineService 리팩터링

### 기능목록

- [X] LineSectionAcceptanceTest 리팩터링
  - [X] 지하철 구간 추가 테스트 새로 작성
  - [X] 기존 구간 등록 테스트를 삭제한다
  - [X] 지하철 구간 삭제 테스트 새로 작성
  - [X] 기존 구간 삭제 테스트를 삭제한다
  - [X] INTERNAL_SERVER_ERROR 에러 반환을 BAD_REQUEST 반환으로 변경
- [X] LineService 리팩터링
  - [X] Sections에 getStations() 메소드 추가
  - [X] RuntimeException -> CustomException 변경
  - [X] 구간 추가 기능을 Sections로 이전
  - [X] 구간 삭제 기능을 Sections로 이전
  - [X] 2depth 코드 리팩토링
  - [X] Distance 도메인 생성

## 2단계 - 경로 조회 기능

### 요구사항

- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기

#### Request

```http request
HTTP/1.1 200 
Request method: GET
Request URI: http://localhost:55494/paths?source=1&target=6
Headers: Accept=application/json Content-Type=application/json; charset=UTF-8
```

#### Response

```http request
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

### 기능목록

- [X] 경로를 검색하면 최단거리와 역 순서를 반환한다
  - [X] 인수테스트 작성
  - [X] 도메인 PathFinder 생성
  - [X] Controller, Service 작성
- [X] 예외 상황을 테스트 한다
  - [X] 출발역과 도착역이 같은 경우
  - [X] 출발역과 도착역이 연결이 되어 있지 않은 경우 
  - [X] 존재하지 않은 출발역이나 도착역을 조회 할 경우

## 3단계 - 인증을 통한 기능 구현

### 요구사항

- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

#### 즐겨찾기 기능 구현하기

- 생성 요청/응답
```http request
POST /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
content-type: application/json; charset=UTF-8
content-length: 27
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
{
    "source": "1",
    "target": "3"
}

HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1
```
- 목록 조회 요청/응답
```http request
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: application/json
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

[
    {
        "id": 1,
        "source": {
            "id": 1,
            "name": "강남역",
            "createdDate": "2020-12-27T13:32:26.364439",
            "modifiedDate": "2020-12-27T13:32:26.364439"
        },
        "target": {
            "id": 3,
            "name": "정자역",
            "createdDate": "2020-12-27T13:32:26.486256",
            "modifiedDate": "2020-12-27T13:32:26.486256"
        }
    }
]
```
- 삭제 요청/응답
```http request
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate



HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Sun, 27 Dec 2020 04:32:26 GMT
```

### 기능 목록

- [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
  - [X] AuthAcceptanceTest 인수 테스트 작성
  - [X] 로그인 사용자만 /member/me에 접근
- [X] 인증 - 내 정보 조회 기능 완성하기
- [X] 인증 - 즐겨 찾기 기능 완성하기
  - [X] 즐겨찾기 생성 인수테스트 작성
  - [X] 즐겨찾기 생성 구현
  - [X] 즐겨찾기 목록 조회 인수테스트 작성
  - [X] 즐겨찾기 목록 조회 구현
  - [X] 즐겨찾기 삭제 인수테스트 작성
  - [X] 즐겨찾기 삭제 구현
  - [X] 예외상황 테스트 및 처리
    - [X] 소유자가 다른 즐겨찾기는 조회 할 수 없다
    - [X] 소유자가 다른 즐겨찾기는 삭제 할 수 없다

## 4단계 - 요금 조회

### 요구사항

- 경로 조회 시 거리 기준 요금 정보 포함하기
- 노선별 추가 요금 정책 추가
- 연령별 할인 정책 추가

### 기능목록

- [X] 경로 조회 시 거리 기준 요금 정보 포함하기
  - [X] 인수 테스트 작성
  - [X] 기능 구현
- [X] 노선별 추가 요금 정책 추가
  - [X] 인수 테스트 작성
  - [X] 기능 구현
- [ ] 연령별 할인 정책 추가
  - [ ] 인수 테스트 작성
  - [ ] 기능 구현
