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

# 인수 테스트 기반 TDD
## 1단계 - 인수 테스트 기반 리팩터링
### 요구사항
- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

## 2단계 - 경로 조회 기능
### 요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

### 기능 구현리스트
- [x] 최단 경로 조회 API
- [x] 최단 경로 조회 API관련 Dto
- [x] 최단 경로 조회 서비스
- [x] jgrapht 라이브러리 적용

### 최단 경로 조회기능관련 전문 내용
#### 구간 삭제
<details><summary>Request</summary>

```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```
</details>

<details><summary>Response</summary>

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
</details>

## 3단계 - 인증을 통한 기능 구현
### 요구사항
- [x] 토큰 발급기능(로그인) 인수 테스트 만들기
- [x] 인증기능 만들기
  - [x] 내 정보 조회기능 완성하기
  - [x] 즐겨 찾기 기능 완성하기

### 기능 구현리스트
- [x] 로그인은 이메일과 패스워드로 인증된다.
- [x] 유저정보조회는 이메일 나이가 조회된다.
- [x] 로그인이 성공시 JWT이 발급된다.
- [x] 변조된 JWT로 API요청시 인증 실패가된다.
- [x] 경로 검색에대한 즐겨찾기가 추가된다.
- [x] 경로 검색에대한 즐겨찾기가 조회된다.
- [x] 경로 검색에대한 즐겨찾기가 삭제된다.

### 인증 기능관련 전문 내용
#### 로그인

<details><summary>Request</summary>

```
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```
</details>

<details><summary>Response</summary>

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
</details>

#### 즐겨찾기 생성

<details><summary>Request</summary>

```
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
```
</details>

<details><summary>Response</summary>

```
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1
```
</details>

#### 즐겨찾기 목록조회

<details><summary>Request</summary>

```
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: application/json
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```
</details>

<details><summary>Response</summary>

```
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
</details>

#### 즐겨찾기 삭제

<details><summary>Request</summary>

```
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```
</details>

<details><summary>Response</summary>

```
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Sun, 27 Dec 2020 04:32:26 GMT
```
</details>

## 4단계 - 요금조회
### 요구사항
- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
- [ ] 노선별 추가 요금 정책 추가
- [ ] 연령별 할인 정책 추가

### 기능 구현리스트
- [ ] 이동거리에 따른 요금정책이 부여된다.
  - [ ] 기본운임 가격은 1250원이 된다.
  - [ ] 이동 거리가 10Km이내 시 기본운임이 적용된다.
  - [ ] 이동 거리가 10~50Km 일 시 기본운임 + 5Km마다 100원씩 적용된다.
  - [ ] 이동 거리가 50Km 초과 일 시 기본운임 + 800 + 8Km마다 100원씩 적용된다.
- [ ] 노선에 따른 요금정책이 부여된다.
  - [ ] 경로에 있는 노선중 가장 높은 금액만 적용된다.
- [ ] 연령별 요금할인이 적용된다.
  - [ ] 연령이 6이상 ~ 13미만은 총 운임에서 350원을 제외한 금액의 50%을 할인된다.
  - [ ] 연령이 13이상 ~ 19미만은 총 운임에서 350원을 제외한 금액의 20%을 할인된다.
- [ ] 계산된 요금이 FrontEnd의 경로탐색에 조회된다.