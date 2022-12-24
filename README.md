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

-----------------

## Step1 인수 테스트 기반 리팩터링
### 미션 요구사항
- LineService 리팩터링
- LineSectionAcceptanceTest 리팩터링
### 미션 목적: 리팩터링
- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기
### 미션 요구사항 설명
- Domain으로 옮길 로직 찾기
    * 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 옮기기
    * 객체지향 생활체조 참고
        * 규칙 1: 한 메서드에 오직 한 단계의 들여쓰기(indent)만 한다.
        * 규칙 2: else 예약어를 쓰지 않는다.
        * 규칙 3: 모든 원시값과 문자열을 포장한다.
        * 규칙 4: 한 줄에 점을 하나만 찍는다.
        * 규칙 5: 줄여쓰지 않는다(축약 금지).
        * 규칙 6: 모든 엔티티를 작게 유지한다.
        * 규칙 7: 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
        * 규칙 8: 일급 콜렉션을 쓴다.
        * 규칙 9: 게터/세터/프로퍼티를 쓰지 않는다.
- Domain의 단위 테스트 작성하기
    * 서비스 레이어에서 옮겨올 로직의 기능 테스트
    * SectionTest나 LineTest 클래스가 생성될 수 있음
- 로직을 옮기기
    * 기존 로직을 지우지 않고 새로운 로직 만들어 수행
    * 정상 동작 확인 후 기존 로직 제거
### LineService 리팩터링 대상 기능목록
- [X] 지하철 노선 생성
- [X] 지하철 노선 목록 조회
- [X] 지하철 노선 조회
- [X] 지하철 노선 수정
- [X] 지하철 노선 삭제
- [X] 지하철 노선 내 구간 추가
- [X] 지하철 노선 내 역 삭제(구간 삭제)
#### LineSectionAScceptanceTest 리팩터링
* [X] API 검증이 아닌 시나리오, 흐름을 검증하는 테스트로 리팩터링
* [X] 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러 시나리오 만들어 인수 테스트 작성
#### 인수 조건 예시
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
### Step1 회고 작성
- 미션 요구사항뿐만 아니라, 다른 리팩토링 대상이 있으면 주도적으로 처리필요
  - 요구사항외 이 내용도 바꿔도 될까? 라는 고민이 있으면 합당하다면 수행
- 의미 있는 Commit 단위를 TDD 기반으로 작성 필요
- 메서드 순서 클린코드 기반하여 작성 public / private(호출되는 순서)
-----------------
## Step2 경로 조회 기능
### 미션 요구사항
- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기
### 요청 / 응답 포맷
Request
~~~http request
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
~~~
Response
~~~http request
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
~~~
### 요구사항 기반 구현 목록
- [X] 최단 경로 라이브러리 적용(jgrapht)
    - 정점: 지하철역 - station
    - 간선: 지하철역 연결정보 - section
    - 가중치: 거리 - section 간의 distance
    - 최단 거리 기준 조회 시 가중치를 '거리'로 설정
- [X] 예외사항 적용
    - 출발역과 도착역이 같은 경우
    - 출발역과 도착역이 연결이 되어 있지 않은 경우
    - 존재하지 않은 출발역이나 도착역을 조회할 경우
### 미션 수행 순서
- 인수 테스트 성공 시키기
    - mock 서버와 dto를 정의하여 인수테스트 성공 시키기
~~~text
Feature 지하철 경로 관련 기능
    Background
        Given 지하철역(Station)이 여러개 등록되어 있음
        And 지하철노선(Line) 여러개 등록되어 있음
        And 지하철노선에 지하철역(Section) 여러개 등록되어 있음
    
    Scenario 출발역과 도착역 사이의 최단 경로 조회
        When 지하철 최단 경로 조회 요청
        Then 출발역과 도착역 사이의 최단 경로 조회됨
    
    Scenario 출발역과 도착역이 같을 때 최단 경로 조회
        When 지하철 최단 경로 조회 요청
        Then 최단 경로 조회 실패
    
    Scenario 존재하지 않은 출발역 또는 도착역으로 최단 경로 조회
        When 지하철 최단 경로 조회 요청
        Then 최단 경로 조회 실패
~~~
- 기능 구현
    - TDD의 방향보다 테스트를 통해 구현할 기능을 명세하는것과 리팩터링이 더 중요
    - Outside In 경우
        - 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
        - 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
        - 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
        - Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
    - Inside Out 경우
        - 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
        - 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
        - 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작
    - ex) 경로 조회를 수행하는 도메인 구현 예시
      1. PathFinder 라는 클래스 작성 후 경로 조회를 위한 테스트를 작성
      2. 경로 조회 메서드에서 Line을 인자로 받고 그 결과로 원하는 응답을 리턴하도록 테스트 완성
      3. 테스트를 성공시키기 위해 JGraph의 실제 객체를 활용(테스트에서는 알 필요가 없음)
### jgrapht 라이브러리 테스트코드 예시
~~~java
@Test
public void getDijkstraShortestPath() {
    WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
    List<String> shortestPath 
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

    assertThat(shortestPath.size()).isEqualTo(3);
}
~~~
### Step2 회고
- 우테캠 미션 중 인터페이스를 구현한적이 없었는데, 경로찾는 방식이 바뀔 수도 있다는 생각에 인터페이스로 적용.
- 반신반의했지만, 더 나은 방식을 외부로부터 적용할 수 있다면 인터페이스로 확장성을 열어두는 것을 앞으로도 고려.
- 기존 도메인 중심 TDD 진행 시 fail ->  pass -> refactoring 를 염두해두며 도메인을 설계 및 개발.
- ATDD도 마찬가지로 ATDD의 사이클을 염두해두며 레이어별로 설계 및 구현하는 것을 습관화 필요.
-------------------
## Step3 인증을 통한 기능 구현
### 미션 요구사항
- 토큰 발급 기능(로그인) 인수 테스트 만들기
- 인증 - 내 정보 조회 기능 완성하기
- 인증 - 즐겨찾기 기능 완성하기
### 토큰 발급 인수 테스트
#### 인수조건
~~~ text
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
~~~
-[X] 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
-[X] AuthAcceptanceTest을 만족하도록 구현하면 됨
-[X] AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기
-[X] Bearer Auth 유효하지 않은 토큰 인수 테스트
-[X] 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리
#### 요청/응답
~~~ http request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
}
~~~
### 내 정보 조회 기능
#### 인수조건
- [X] MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
- [X] 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
- [X] 로그인 후 발급 받은 토큰을 포함해서 요청 하기
#### 토큰을 통한 인증
- [X] /members/me 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
- [X] @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용하기
- [X] 아래의 기능이 제대로 동작하도록 구현하기
~~~ java
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
~~~
### 즐겨찾기 기능 구현하기
#### 인수조건
~~~text
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
~~~
#### 생성 요청/응답
요청
~~~ http request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
}
~~~
#### 목록 조회 요청/응답
~~~ http request
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
~~~
~~~ http request
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
~~~
### Step3 회고
- 설계에 대한 고민한 흔적 또한 readme에 적는다면 다른  사람과 협업 시에 도움이 될 수 있다.
    - commit 메세지로는 부족하고 이력 찾기가 힘들 수 있음
## Step4 요금조회
### 미션 요구사항
- [X] 경로 조회 시 거리 기준 요금 정보 포함하기
    - [X] 경로조회, PathService.findTheShortestPath 에 구현
    - [X] 기본 생성(기본 운임)은 1,250원
    - [X] 이용거리 요청 > 구간별 금액 계산 > 이용거리 초과 시 운임 적용
- [X] 노선별 추가 요금 정책 추가
    - [X] Line에 추가 요금 필드 추가, Line 생성 시 추가(Default 0)
    - [X] Fare 팩토리메서드에 추가요금 필드 있는 경우, 기본요금 + 추가요금 세팅
    - [X] 요금 생성 API에 추가요금을 처리할 수 있도록 함
    - [X] Section Line을 전달받아, 각 Line의 추가요금 Max를 + 적용
- [X] 연령별 할인 정책 추가
- [X] /paths 요청 시 LoginMember 객체 처리
    - [X] 비로그인 시 예외처리 이미 적용되어 있음(로그인 시 토큰정보가 없으면 예외 발생)
    
### 요구사항 설명
#### 거리별 요금 정책이 존재
- 기본운임(10㎞ 이내) : 기본운임 1,250원
- 이용 거리초과 시 추가운임 부과
    - 10km초과∼50km까지(5km마다 100원)
    - 50km초과 시 (8km마다 100원)

#### 노선별 추가 요금정책
- 노선에 추가 요금 필드를 추가
- 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
- 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
#### 로그인 사용자의 경우 연령별 요금 할인 적용
- 청소년(13세 이상~19세 미만): 운임에서 350원을 공제한 금액의 20%할인
- 어린이 ( 6세 이상~13세 미만): 운임에서 350원을 공제한 금액의 50%할인

### 미션 인수조건
~~~
Feature: 지하철 경로 검색

  Scenario: 두 역의 최단 거리 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
    Then 최단 거리 경로를 응답
    And 총 거리도 함께 응답함
    And ** 지하철 이용 요금도 함께 응답함 **
~~~
