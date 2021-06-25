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

---

# Step1, 인수 테스트 기반 리팩토링

## 요구사항

- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

## 요구사항 설명

### 인수 테스트 통합

- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

```text
Feature: 지하철 구간 관련 기능

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음

  Scenario: 지하철 구간을 관리
    When 지하철 구간 등록 요청
    Then 지하철 구간 등록됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 등록한 지하철 구간이 반영된 역 목록이 조회됨
    When 지하철 구간 삭제 요청
    Then 지하철 구간 삭제됨
    When 지하철 노선에 등록된 역 목록 조회 요청
    Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨
```

### 인수 테스트 기반 리팩터링

1. LineService의 비즈니스 로직을 도메인으로 옮기기
    - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 이관
    - 단위 테스트 작

2. 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기

3. 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
    - 기존 로직을 지우지 않고 새 로직 작성
    - 새 로직이 정상 작동하면 기존 로직 삭제

---

# Step2, 경로 조회 기능

## 요구사항

- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

## 요청/응답

1. 요청
    ```text
    HTTP/1.1 200 
    Request method:	GET
    Request URI:	http://localhost:55494/paths?source=1&target=6
    Headers: 	    Accept=application/json
                    Content-Type=application/json; charset=UTF-8
    ```

2. 응답
    ```text
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

## 예외 상황

- 출발역과 도착역이 같은 경우
- 출발역과 도착역이 연결이 되어 있지 않은 경우
- 존재하지 않은 출발역이나 도착역을 조회 할 경우

---

# Step3, 인증을 통한 기능 구현

## 요구사항

- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [x] 인증 - 내 정보 조회 기능 완성하기
- [x] 인증 - 즐겨 찾기 기능 완성하기

## 요구사항 설명

### 토큰 발급 인수 테스트

- 토급 발급(로그인 검증) 인수 테스트 작성
- 이메일과 패스워드를 이용하여 요청 시 access token 응답
- `AuthAcceptanceTest` 인수 테스트 작성

#### 인수 조건

```text
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

#### 요청 정보

```text
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

#### 응답 정보

```text
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

### 내 정보 조회 기능

#### 인수 테스트

- `MemberAcceptanceTest::manageMyInfo` 에 인수 테스트를 추가하기
- 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
- 로그인 후 발급 받은 토큰을 포함해서 요청 하기

#### 토큰을 통한 인증

- `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
- `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용하기
- 아래의 기능이 제대로 동작하도록 구현하기

```java
class MemberController {
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
}
```

### 즐겨찾기 기능 구현

- 즐겨찾기 기능 구현 및 인증 포함하여 전체 ATDD 사이클 경험

#### 인수 조건

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

#### 생성 요청/응답

```text
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

```text
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

```text
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

# Step4, 요금 조회

## 요구사항

- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
- [x] 노선별 추가 요금 정책 추가
- [x] 연령별 할인 정책 추가

## 요구사항 설명

### 거리별 요금 정책

- 기본운임(10 ㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과 (실제 이동 경로가 아니라 최단거리 기준으로 계산)
   - 10 km 초과 ∼ 50 km 까지(5 km 마다 100원)
   - 50 km 초과 시 (8 km 마다 100원)

#### 수정된 인수 조건 (최단거리 탐색)

```text
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리도 함께 응답함
    And ** 지하철 이용 요금도 함께 응답함 **
```

### 노선별 추가 요금 정책

- 노선에 추가 요금 필드 추가
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

### 로그인 사용자는 연령별 요금 할인 정책 적용

- 어린이: 운임에서 350원을 공제한 금액의 50% 할인
    - 6세 이상 ~ 13세 미만
- 청소년: 운임에서 350원을 공제한 금액의 20% 할인
    - 13세 이상 ~ 19세 미만
