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

#### LineSectionAcceptanceTest 리팩터링

- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

**인수 조건 예시**
```
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

#### LineService 리팩터링

- 인수 테스트 기반 리팩터링

    - LineService의 비즈니스 로직을 도메인으로 옮기기
    - 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
    - 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

- 단계별 적용

  1.Domain으로 옮길 로직을 찾기

    - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
    - 객체지향 생활체조를 참고

  2.Domain의 단위 테스트를 작성하기

    - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
    - SectionsTest나 LineTest 클래스가 생성될 수 있음

  3.로직을 옮기기

    - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
    - 정상 동작 확인 후 기존 로직 제거
    
## 2단계 - 경로 조회 기능

### 요구사항

#### 1. 최단 경로 조회 인수 테스트 만들기

- mock 서버와 dto를 정의하여 인수 테스트 성공 시키기

**요청 / 응답 포맷 참고**

- 요청 포맷(Request)

```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

- 응답 포맷(Response)

```
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "distance": 5,
    "stations": [
        {
            "id": 3,
            "name": "교대역",
            "createdDate": "2021-12-03T16:51:50.659185",
            "modifiedDate": "2021-12-03T16:51:50.659185"
        },
        {
            "id": 4,
            "name": "남부터미널역",
            "createdDate": "2021-12-03T16:51:50.691795",
            "modifiedDate": "2021-12-03T16:51:50.691795"
        },
        {
            "id": 2,
            "name": "양재역",
            "createdDate": "2021-12-03T16:51:50.609985",
            "modifiedDate": "2021-12-03T16:51:50.609985"
        }
    ]
}
```

#### 2. 최단 경로 조회 기능 구현하기

- 최단 경로 라이브러리 ```jgrapht``` 활용
- 학습테스트를 통해 라이브러리 사용법을 익히고 구현하고자 하는 기능에 적용
- 인수테스트를 기반으로 TDD 방식으로 도메인 기능을 구현
- Happy 케이스 구현
  - 출발역과 도착역이 같은 경우 예외처리하지 않고 ```distance:0 , stations:단일역``` 으로 리턴
- 예외 케이스 구현
  - 출발역과 도착역이 연결되어 있지 않은 경우
  - 존재하지 않는 출발역이나 도착역을 조회할 경우

## 3단계 - 인증을 통한 기능 구현

### 요구사항

#### 1. 토큰 발급 기능 (로그인) 인수 테스트 만들기

- 세부 요구사항
  - 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
  - `AuthAcceptanceTest`을 만족하도록 구현하면 됨
  - `AuthAcceptanceTest`에서 제시하는 예외 케이스도 함께 고려하여 구현하기
  - Bearer Auth 유효하지 않은 토큰 인수 테스트
    - 유효하지 않은 토큰으로 `/members/me` 요청을 보낼 경우에 대한 예외 처리

- 인수 조건

```
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

- 요청(request)

```json
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

- 응답(response)

```json
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

#### 2. 인증 - 내 정보 조회 기능 완성하기

- 인수 테스트
  - MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
  - 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
  - 로그인 후 발급 받은 토큰을 포함해서 요청 하기

```java
@DisplayName("나의 정보를 관리한다.")
@Test
void manageMyInfo() {

}
```

- 토큰을 통한 인증
  - `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
  - `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용하기
  - 아래의 기능이 제대로 동작하도록 구현하기

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



#### 3. 인증 - 즐겨찾기 기능 완성하기

- 세부 요구사항
  - 즐겨찾기 기능을 완성하기
    - 즐겨찾기 생성
    - 즐겨찾기 목록 조회
    - 즐겨찾기 삭제
      - 없는 즐겨찾기를 삭제 요청하는 경우 예외
      - 사용자의 즐겨찾기가 아닌 경우 예외
  - 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

- 인수 조건

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



- 생성
  - 요청

```json
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
- 
  - 응답

```json
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1
```

- 목록조회
  - 요청

```json
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: application/json
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```

-
  - 응답

```json
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

- 삭제
  - 요청

```json
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```

-
  - 응답

```json
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Sun, 27 Dec 2020 04:32:26 GMT
```


## 4단계 - 요금 조회

### 요구사항

#### 1. 경로 조회 시 거리 기준 요금 정보 포함하기

**거리별 요금 정책**

- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
  - 10km초과∼50km까지(5km마다 100원)
  - 50km초과 시 (8km마다 100원)



**수정된 인수 조건**

```
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리도 함께 응답함
    And 지하철 이용 요금도 함께 응답함
```



#### 2. 노선별 추가 요금 정책 추가

**노선별 추가 요금 정책**

- 노선에 `추가 요금` 필드를 추가

- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가

  - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
  - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원

- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용

  - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원



#### 3. 연령별 할인 정책 추가

**로그인 사용자의 경우 연령별 요금 할인 적용**

- 청소년: 운임에서 350원을 공제한 금액의 20%할인
- 어린이: 운임에서 350원을 공제한 금액의 50%할인

**! 할인된 금액은 소수 첫번째 자리에서 반올림하여 소수점 금액이 발생하지 않도록 한다.**

```plaintext
- 청소년: 13세 이상~19세 미만
- 어린이: 6세 이상~ 13세 미만
```

