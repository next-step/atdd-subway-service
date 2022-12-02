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

# 🚀 1단계 - 인수 테스트 기반 리팩터링
## 요구사항
- [x] LineService 리팩터링
- [x] LineSectionAcceptanceTest 리팩터링

## 작업사항
- LineAcceptanceTest 테스트 목록 
  - 지하철 노선 생성
    - 기존의 존재하는 노선명으로 생성시 예외
  - 지하철 노선 목록 조회
  - 지하철 노선 조회
  - 지하철 노선 수정
  - 지하철 노선 제거
- LineService 기능 목록
  - saveLine - 노선 저장
  - findLines - 노선 목록 조회
  - findLineById - 아이디로 지하철 노선 단일 조회
  - findLineResponseById - 아이디로 지하철 노선 저회 후 LineResponse로 반환
  - updateLine - 노선 이름과 색 업데이트
  - deleteLineById - 노선 삭제
  - addLineStation - 구간 추가 -> Line으로 비즈니스 로직 이동
  - removeLineStation - 구간 삭제 -> Line으로 비즈니스 로직 이동
  - ~~getStations - 노선에 추가된 역 조회~~
  - ~~findUpStation - 상행 종점 조회~~
- Line 기능 목록
  - addLineStation - 구간 추가
  - removeLineStation - 구간 삭제
  - getStations - 노선에 추가된 역 조회
  - findUpStation - 상행 종점 조회

### 요구사항 설명
#### 인수 테스트 기반 리팩터링
1. Domain으로 옮길 로직을 찾기
   - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   - 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   - SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거

#### 인수 테스트 통합
- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
---
# 🚀 2단계 - 경로 조회 기능

## 요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

## 요청 / 응답 포맷
### Request
```http request
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8
```
### Response
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
--- 

# 🚀 3단계 - 인증을 통한 기능 구현

## 요구사항
- [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] 인증 - 내 정보 조회 기능 완성하기
- [ ] 인증 - 즐겨 찾기 기능 완성하기
---
## 요구사항 설명

### 1. 토큰 발급 인수 테스트
- 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
- `AuthAcceptanceTest` 인수 테스트 만들기
#### 인수 조건
```
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```
#### 요청/응답
##### request
```http request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```
#### response
```http request
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
### 유의사항
- 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
- AuthAcceptanceTest을 만족하도록 구현하면 됨
- AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기
- 유효하지 않은 토큰으로 `/members/me` 요청을 보낼 경우에 대한 예외 처리

---
### 내 정보 조회 기능
#### 인수 테스트
- MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
- 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
- 로그인 후 발급 받은 토큰을 포함해서 요청 하기
```java
@DisplayName("나의 정보를 관리한다.")
@Test
void manageMyInfo() {

}
```
#### 토큰을 통한 인증
`/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
`@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용하기
아래의 기능이 제대로 동작하도록 구현하기
```java
@GetMapping("/members/me")
public ResponseEntity<MemberResponse> findMemberOfMine(LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember.getId());
        return ResponseEntity.ok().body(member);
        }

@PutMapping("/members/me")
public ResponseEntity<MemberResponse> updateMemberOfMine(LoginMember loginMember, @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember.getId(), param);
        return ResponseEntity.ok().build();
        }

@DeleteMapping("/members/me")
public ResponseEntity<MemberResponse> deleteMemberOfMine(LoginMember loginMember) {
        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
        }

```
--- 
### 즐겨 찾기 기능 구현하기
- 즐겨찾기 기능을 완성하기
- 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기
#### 인수 조건
```
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
#### 생성 요청/응답
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
#### 목록 조회 요청/응답
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
#### 삭제 요청/응답
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
