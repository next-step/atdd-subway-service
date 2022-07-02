3단계-인증을 통한 기능 구현
===

# 요구 사항 
요구사항
- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨찾기 기능 완성하기

# 요구사항 설명

### 토큰 발급 인수 테스트
- 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
- `AuthAcceptanceTest` 인수 테스트 만들기

##### 인수 조건
```text
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

##### 요청/응답

request
```http request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```
response
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
- 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
- AuthAcceptanceTest 를 만족하도록 구현하면 됨
- AuthAcceptanceTest 에서 제시하는 여러 케이스도 함께 고려하여 구현하기

##### Bearer Auth 유효하지 않은 토큰 인수 테스트
- 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리

---

### 내 정보 조회 기능

##### 인수테스트
- MemberAcceptanceTest 클래스의 manageMyInfo 메서드에 인수테스트 추가하기
- 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
- 로그인 후 발급 받은 토큰을 포함해서 요청 하기

##### 토큰을 통한 인증
- /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
- @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
- 아래의 기능이 제대로 동작하도록 구현하기
```
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

##### 인수 조건
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

##### 즐겨찾기 생성 요청/응답
Request
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

```

Response
```http request
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1
```

##### 즐겨찾기 목록 조회 요청/응답
Request
```
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: application/json
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```

Response
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

### 삭제 요청/응답
Request
```
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```

Response
```
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Sun, 27 Dec 2020 04:32:26 GMT
```

---

# TODO List

## 목표: 인수테스트를 작성한 후, TDD를 기반으로 기능 구현

### 토큰 발급 인수 테스트
- 요청 URL : POST `/login/token`
- 구현 기능 목록
  - [x] 토큰 발급(로그인) 기능 구현 (로그인 요청 : email, password)
  - [x] 주어진 `AuthAcceptanceTest`통과 하도록 구현
  - [x] 주어진 `AuthAcceptanceTest`의 예외 케이스 구현

### 내 정보 조회 기능
- Base URL : `/members/me`
- 주의 사항 : 로그인 후 발급 받은 토큰을 포함해서 요청
- 구현 기능 목록
  - [x] 내 정보 조회 : GET `/members/me`
  - [x] 내 정보 수정 : PUT `/members/me`
  - [x] 내 정보 삭제 : DELETE `/members/me`
  - [x] 잘못된 토큰 정보로 내 정보 관련 API 요청 시 예외 발생 검증 테스트 구현

### 즐겨 찾기 기능 구현
- Base URL : `/favorites`
- 주의 사항 : 로그인 후 발급 받은 토큰을 포함해서 요청
- 구현 기능 목록 
  - [x] 즐겨 찾기 생성 : POST `/favorites` (즐겨 찾기 생성 요청 : source: Long, target: Long)
  - [x] 즐겨 찾기 목록 조회 : GET `/favorites`
  - [x] 즐겨 찾기 삭제 : DELETE `/favorites/{favoritesId}`
  - [x] 잘못된 토큰 정보로 즐겨찾기 관련 API 요청 시 예외 발생 검증 테스트 구현
---
