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
- [ ] 토큰 발급기능(로그인) 인수 테스트 만들기
- [ ] 인증기능 만들기
  - [ ] 내 정보 조회기능 완성하기
  - [ ] 즐겨 찾기 기능 완성하기

### 기능 구현리스트
- [ ] 로그인은 이메일과 패스워드로 인증된다.
- [ ] 유저정보조회는 이메일 나이가 조회된다.
- [ ] 로그인이 성공시 JWT이 발급된다.
- [ ] 변조된 JWT로 API요청시 예외가 발생된다.
- [ ] 경로 검색에대한 즐겨찾기가 추가된다.
- [ ] 경로 검색에대한 즐겨찾기가 조회된다.
- [ ] 경로 검색에대한 즐겨찾기가 삭제된다.

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