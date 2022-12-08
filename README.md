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

***

## 🚀1단계 - 인수 테스트 기반 리팩터링
<details>
<summary> </summary>

### 요구사항
* [x] LineService 리팩터링
* [x] (선택) LineSectionAcceptanceTest 리팩터링

### 요구사항 설명
#### 인수 테스트 기반 리팩터링
* LineService의 비즈니스 로직을 도메인으로 옮기기
    * Sections로
        * [x] addSection
        * [x] updateUpStation
        * [x] updateDownStation
        * [x] updateDownStation
        * [x] getStations
        * [x] remove
    * Section으로
        * [x] updateUpStation
        * [x] updateDownStation
* 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
* 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

1. **Domain으로 옮길 로직을 찾기**
    * 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
    * 객체지향 생활체조를 참고
2. **Domain의 단위 테스트를 작성하기**
    * 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
    * SectionsTest나 LineTest 클래스가 생성될 수 있음
3. **로직을 옮기기**
    * 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
    * 정상 동작 확인 후 기존 로직 제거

#### (선택) 인수 테스트 통합
* API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
* 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

인수 조건 예시
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
</details>

---

## 🚀2단계 - 경로 조회 기능
<details>
<summary> </summary>

### 요구사항
* [x] 최단 경로 조회 인수 테스트 만들기
* [x] 최단 경로 조회 기능 구현하기

### 요청/응답 포맷
**Request**
```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```   

**Response**
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

### 힌트
<details>
<summary> </summary>

####최단 경로 라이브러리
* jgrapht 라이브러리를 활용하면 간편하게 최단거리를 조회할 수 있음
* 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
    * 정점: 지하철역(Station)
    * 간선: 지하철역 연결정보(Section)
    * 가중치: 거리
* 최단 거리 기준 조회 시 가중치를 `거리`로 설정

```
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
```

#### 외부 라이브러리 테스트
* 외부 라이브러리의 구현을 수정할 수 없기 때문에 단위 테스트를 하지 않음
* 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야 함
* 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용

**인수 테스트 픽스쳐 예시**
```
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }
    ...
```

####예외 상황 예시
* 출발역과 도착역이 같은 경우
* 출발역과 도착역이 연결이 되어 있지 않은 경우
* 존재하지 않은 출발역이나 도착역을 조회 할 경우

####미션 수행 순서
**인수 테스트 성공 시키기**
* mock 서버와 dto를 정의하여 인수 테스트 성공 시키기   
  `TDD의 방향보다 테스트를 통해 구현할 기능을 명세하는것과 리팩터링이 더 중요합니다!`

Outside In 경우
* 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
* 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
* 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
* Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)

Inside Out 경우
* 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
* 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
* 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작
```
  - 1. PathFinder 라는 클래스 작성 후 경로 조회를 위한 테스트를 작성
  - 2. 경로 조회 메서드에서 Line을 인자로 받고 그 결과로 원하는 응답을 리턴하도록 테스트 완성
  - 3. 테스트를 성공시키기 위해 JGraph의 실제 객체를 활용(테스트에서는 알 필요가 없음)
```
`두 방향성을 모두 사용해보시고 테스트가 협력 객체의 세부 구현에 의존하는 경우(가짜 협력 객체 사용)와 테스트 대상이 협력 객체와 독립적이지 못하고 변경에 영향을 받는 경우(실제 협력 객체 사용)를 모두 경험해보세요 :)`

####프론트엔드
* 구현한 기능이 정상적으로 동작하는지 확인을 하기 위한 코드입니다.
* 반드시 페이지에 맞게 설계를 할 필요는 없고 프론트엔드 코드를 수정해도 무방합니다.

</details>
</details>

---

## 🚀 3단계 - 인증을 통한 기능 구현
<details>
<summary> </summary>

### 요구사항
* [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
* [x] 인증 - 내 정보 조회 기능 완성하기
* [x] 인증 - 즐겨 찾기 기능 완성하기

### 요구사항 설명

#### 토큰 발급 인수 테스트
* 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
* AuthAcceptanceTest 인수 테스트 만들기

**인수 조건**
```
Feature: 로그인 기능

Scenario: 로그인을 시도한다.
Given 회원 등록되어 있음
When 로그인 요청
Then 로그인 됨
```

**요청/응답**   
request
```
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

response
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
* 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기
* AuthAcceptanceTest을 만족하도록 구현하면 됨
* AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기

**Bearer Auth 유효하지 않은 토큰 인수 테스트**
* 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리


####내 정보 조회 기능
**인수 테스트**
* MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
* 내 정보 조회, 수정, 삭제 기능을 /members/me 라는 URI 요청으로 동작하도록 검증
* 로그인 후 발급 받은 토큰을 포함해서 요청 하기
```
@DisplayName("나의 정보를 관리한다.")
@Test
void manageMyInfo() {

}
```
**토큰을 통한 인증**
* `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
* `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용하기
* 아래의 기능이 제대로 동작하도록 구현하기
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

####즐겨 찾기 기능 구현하기
* 즐겨찾기 기능을 완성하기
* 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

**인수 조건**
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

생성 요청/응답
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


목록 조회 요청/응답
```
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

삭제 요청/응답
```
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

### 힌트
**인증 기반 인수 테스트**
* 사용자 정보를 인수 테스트 메서드의 첫번째 파라미터로 넘겨줄 수 있음

```
@BeforeEach
public void setUp() {
    ...

    회원_생성을_요청(EMAIL, PASSWORD, 20);
    사용자 = 로그인_되어_있음(EMAIL, PASSWORD);
}

@DisplayName("즐겨찾기를 관리한다.")
@Test
void manageMember() {
    // when
    ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 정자역);
    ...
}
```
* 참고로 코틀린에서는 확장 함수를 활용하여 작성할 수 있음

```
val 사용자 = RestAssured.given().log().all().auth().oauth2(accessToken)

@Test
fun 즐겨찾기_관리_기능() {
    val response = 사용자.즐겨찾기_생성_요청(강남역, 정자역)
    ...
}
```

```
fun RequestSpecification.즐겨찾기_생성_요청(
    source: Long,
    target: Long
): ExtractableResponse<FavoriteResponse> {
    val favoriteRequest = FavoriteRequest(source, target)

    return this
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(favoriteRequest)
        .`when`().post("/favorites")
        .then().log().all()
        .extract()
}
```

</details>


## 🚀 4단계 - 요금 조회
<details>
<summary> </summary>

### 요구사항
* [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
* [ ] 노선별 추가 요금 정책 추가
* [ ] 연령별 할인 정책 추가

### 요구사항 설명
#### 거리별 요금 정책
* 기본운임(10㎞ 이내) : 기본운임 1,250원
* 이용 거리초과 시 추가운임 부과
  * 10km초과∼50km까지(5km마다 100원)
  * 50km초과 시 (8km마다 100원)
  
**지하철 운임은 거리비례제로 책정됩니다. (실제 이동한 경로가 아닌 최단거리 기준으로 계산)**

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
    And ** 지하철 이용 요금도 함께 응답함 **

```

### 노선별 추가 요금 정책
* 노선에 추가 요금 필드를 추가
* 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  * ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
  * ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
* 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  * ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

### 로그인 사용자의 경우 연령별 요금 할인 적용
  * 청소년: 운임에서 350원을 공제한 금액의 20%할인
  * 어린이: 운임에서 350원을 공제한 금액의 50%할인
```
- 청소년: 13세 이상~19세 미만
- 어린이: 6세 이상~ 13세 미만
```

### 힌트
#### /paths 요청 시 LoginMember 객체 처리
* 로그인 시 LoginMember 객체 를 활용하여 연령별 요금 할인을 적용할 수 있음
* 비 로그인 시 LoginMember는 비어있는 객체가 넘어가므로 별도의 처리가 필요함
* 필요 시 아래 구문에서 null object를 리턴해주는 부분을 예외를 던지도록 수정해도 무방함
```
public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            return new LoginMember(); // <--- 이 부분 변경 가능
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
```

### 5km 마다 100원 추가 로직
```
    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
```

</details>

