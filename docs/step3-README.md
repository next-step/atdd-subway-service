# Step3 - 인증을 통한 기능 구현

## 요구사항

- 토큰 발급 기능 (로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨 찾기 기능 완성하기

## 요구사항 설명

### 토큰 발급 인수 테스트

- 토큰 발급을 검증하는 인수 테스트 작성
- `AuthAccptanceTest` 인수 테스트 작성

#### 인수 조건

- 로그인 성공
```text
Feature: 로그인 기능
  Scenario: 로그인 시도
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 완료
    
  Scenario: 로그인 시도 - 실패
    Given 존재하지 않은 회원정보
    When 로그인 요청
    Then 로그인 실패
    
  Scenario: 로그인 시도 - 실패
    Given 회원 등록되어 있음
    And 비밀번호가 틀린 회원정보
    When 로그인 요청
    Then 로그인 실패
    
  Scenario: 로그인 시도 - 실패
    Given 유효하지 않은 토큰정보
    When 내 정보 조회
    Then 정보 조회 실패
```

#### 요청 / 응답

- request

```http request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
  "password": "password",
  "email": "email@email.com"
}
```

- response

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

- 이메일과 패스워드를 이용하여 요청 시 access token 을 응답하는 기능 구현
- `AuthAcceptanceTest` 만족 하도록 구현
- `AuthController` 에서 제시하는 예외 케이스도 함께 고려하여 구현

## 내 정보 조회 기능

### 인수 테스트

- `MemberAccpetanceTest` 클래스의 `manageMyInfo` 메서드에 인수 테스트 추가
- 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
- 로그인 후 발급 받은 토큰 포함해서 요청

### 토큰을 통한 인증

- `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
- `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용

## 즐겨찾기 기능 구현

- 즐겨찾기 기능 완성
- 전체 ATDD 사이클 경험할 수 있도록 기능 구현

### 인수 조건

```text
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

### 생성 요청/응답

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

### 목록 조회 요청/응답
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

### 삭제 요청/응답
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

---

## 구현 기능 목록

> Outside-In, Inside-Out 방식을 통해 ATDD 사이클을 경험

### 토큰 발급 인수 테스트
- [x] 토큰 발급(로그인) 기능 인수 테스트 작성
- [x] 토큰 발급(로그인) 기능 구현

### 내 정보 조회 기능
- [x] 내 정보 조회 기능 인수 테스트 작성
- [x] 내 정보 조회 기능 구현
  - [x] 내 정보 조회
  - [x] 내 정보 수정
  - [x] 내 정보 삭제
  - [x] 토큰 정보 확인 되지 않으면 예외 처리

### 즐겨 찾기 기능 구현
- [x] 즐겨찾기 생성 기능 인수 테스트 작성
- [ ] 즐겨찾기 생성 기능 구현
  - [ ] 즐겨찾기 생성
    - [ ] 즐겨찾기 생성 시 중복 예외 처리
    - [ ] 즐겨찾기 생성 시 토큰 정보 확인 되지 않으면 예외 처리
    - [ ] 즐겨찾기 생성 시 존재하지 않는 출발역 예외 처리
    - [ ] 즐겨찾기 생성 시 존재하지 않는 도착역 예외 처리
    - [ ] 즐겨찾기 생성 시 출발역과 도착역이 같은 경우 예외 처리
    - [ ] 즐겨찾기 생성 시 출발역과 도착역이 연결되어 있지 않은 경우 예외 처리
  - [ ] 즐겨찾기 조회
    - [ ] 즐겨찾기 조회 시 토큰 정보 확인 되지 않으면 예외 처리