# 1단계 - 인수 테스트 기반 리펙터링

## 요구사항

* LineService 리펙터링
* (선택) LineSectionAcceptanceTest 리펙터링

## 요구사항 설명

### 인수테스트 기반 리펙터링

* LineService의 비즈니스 로직을 도메인으로 옮기기
* 한 번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리펙터링하기
* 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리펙터링하기

### 1. Domain으로 옮길 로직 찾기

* Spring Bean을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
* 객체지향 생활체조 참고

### 2. Domain의 단위 테스트를 작성하기

* 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
* SectionsTest나 LineTest 클래스가 생성될 수 있음

### 3. 로직 옮기기

* 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
* 정상 동작 확인 후 기존 로직 제거

## (선택) 인수 테스트 통합

* API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리펙터링 하기
* 반드시 하나의 시나리오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

### 인수 조건 예시

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

# 2단계 - 경로 조회 기능

## 요구사항

* 최단 경로 조회 인수 테스트 만들기
* 최단 경로 조회 기능 구현하기

<details>
    <summary>요청/응답 포맷</summary>

### Request

``` 
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

### Response

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

## 힌트

### 최단 경로 라이브러리 (jgrapht)

* jgrapht 라이브러리를 활용하면 간편하게 최단 거리 조회 가능
* 정점(vertex)과 간선(edge), 그리고 가중치 개념을 사용
    * `정점 : Station(지하철역)`
    * `간선 : Section(구간)`
    * `가중치 : Distance(거리)`
* 최단 거리 기준 조회 시 가중치를 `Distnacen(거리)`로 설정

### 외부 라이브러리 테스트

* 외부 라이브러리의 구현을 수정할 수 없기 떄문에 단위테스트를 하지 않음
* 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야함
* 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용

### 예외 상황

* 출발역과 도착역이 같은 경우
* 출발역과 도착역이 연결이 되어 있지 않은 경우
* 존재하지 않는 출발역이나 도착역을 조회할 경우

## 미션 수행 순서

* 인수 테스트 성공시키기
    * mock 과 dto를 정의하여 인수 테스트 성공시키기
* Outside In 경우
    * Controller 구현 이후 service 구현 시 서비스 테스트 우선 작성 후 기능 구현
    * service 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
    * 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
    * Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
* Inside Out 경우
    * 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
    * 해당 도메인의 단위 테스트를 통해 도멩니의 역할과 경계를 설계
    * 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작

``` 
ex) 경로 조회를 수행하는 도메인 구현 예시
  - 1. PathFinder 라는 클래스 작성 후 경로 조회를 위한 테스트를 작성
  - 2. 경로 조회 메서드에서 Line을 인자로 받고 그 결과로 원하는 응답을 리턴하도록 테스트 완성
  - 3. 테스트를 성공시키기 위해 JGraph의 실제 객체를 활용(테스트에서는 알 필요가 없음)
```

# 3단계 - 인증을 통한 기능 구현

## 요구 사항

* 토큰 발급 기능 (로그인) 인수 테스트 만들기
* 인증 - 내 정보 조회 기능 완성하기
* 인증 - 즐겨 찾기 기능 완성하기

## 토큰 발급 인수 테스트

* 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
* `AuthAcceptanceTest` 인수 테스트 만들기

### 인수 조건

``` 
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

<details>
  <summary>요청/응답</summary>

* request

``` 
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "email": "email@email.com",
    "password": "password"
}
```

* response

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

* 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현
* `AuthAcceptanceTest`를 만족하도록 구현
* `AuthAcceptanceTest`에서 제시하는 예외 케이스도 함께 고려하여 구현

### Bearer Auth 유효하지 않은 토큰 인수 테스트

* 유효하지 않은 토큰으로 `/members/me` 요청을 보낸 경우에 대한 예외 처리

## 내 정보 조회 기능

### 인수 테스트

* `MemberAcceptanceTest` 클래스의 `manageMyInfo` 메서드에 인수 테스트 추가하기
* 내 정보 조회, 수정, 삭제 기능을 `/members/me` URL 요청으로 동작하도록 검증
* 로그인 후 발급 받은 토큰을 포함하여 요청하기

``` 
@DisplayName("나의 정보를 관리한다.")
@Test
void manageMyInfo() {

}
```

### 인수 조건

``` 
Scenario: 나의 정보를 관리
    When 회원 생성을 요청
    Then 회원 생성됨
    When Bearer Auth 인증 요청
    Then Bearer Auth 인증 성공
    When 내 정보 조회 요청
    Then 내 정보 조회됨
    When 내 정부 수정 요청
    Then 내 정보 수정됨
    When 내 정부 삭제 요청
    Then 내 정보 삭제됨
```

### 토큰을 통한 인증

* `/members/me` 요청 시 토큰을 확인하여 로그인 정보를 받아올 수 있도록 하기
* `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`를 활용하기
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

## 즐겨 찾기 기능 구현하기

* 즐겨 찾기 기능을 완성하기
* 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기

### 인수 조건

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

### 생성 요청/응답

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

HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1

```

### 목록 조회 요청/응답

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

### 삭제 요청/응답

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

## 힌트

### 인증 기반 인수 테스트

* 사용자 정보를 인수 테스트의 첫 번째 파라미터로 넘겨줄 수 있음

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

* 코틀린에서 사용하는 확장 함수 참고

``` 
val 사용자 = RestAssured.given().log().all().auth().oauth2(accessToken)

@Test
fun 즐겨찾기_관리_기능() {
    val response = 사용자.즐겨찾기_생성_요청(강남역, 정자역)
    ...
}
 
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

# 4단계 - 요금 조회

## 요구사항

* 경로 조회 시 거리 기준 요금 정보 포함하기
* 노선별 추가 요금 정책 추가
* 연령별 할인 정책 추가

### 거리별 요금 정책

* 기본 운임(10km 이내) : 1250원
* 10km 초과 ~ 50km 이하 : 기본료 + (5km마다 100원)
* 50km 초과 : 기본료 + (8km마다 100원)

### 수정된 인수 조건

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

* 노선에 `추가 요금` 필드를 추가
* 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가
    * 900원의 추가요금이 있는 노선 8km 이용 시
        * 1250(기본료) + 900(추가요금) + 0(10km 이내) : 2150원
    * 900원의 추가 요금이 있는 노선 12km 이용 시
        * 1250(기본료) + 900(추가요금) + 100(10km 초과 ~ 50km 이하 - 5km 마다 100원) : 2250원
* 경로 중 추가요금이 있는 노선을 환승하여 이용할 경우 가장 높은 금액의 추가 요금만 적용
    * ex) 500원, 900원의 추가요금이 있는 노선을 경유하여 8km 이용시 = 1250(기본료) + 900(가장 높은 추가요금이 있는 노선의 추가요금) + 0(10km 이내)

### 로그인 사용자의 경우 연령별 요금 할인 적용

* 어린이 : 운임에서 350원을 공제한 금액의 50% 할인
    * 대상 : 6세 ~ 12세
    * 예시
        * 900원의 추가 요금이 있는 노선 8km 이용 시
            * 450((1250 - 350) * 0.5 ) + 900 + 0(10km 이내) : 1350원
        * 900원의 추가 요금이 있는 노선 12km 이용 시
            * 450((1250 - 350) * 0.5 ) + 900 + 100(10km 초과 ~ 50km 이하 - 5km 마다 100원) : 1450원
* 청소년 : 운임에서 350원을 공제한 금액의 20% 할인
    * 대상 : 13세 ~ 18세
    * 예시
        * 900원의 추가 요금이 있는 노선 8km 이용 시
            * 720((1250 - 350) * 0.8) + 900 + 0(10km 이내) : 1620원
        * 900원의 추가 요금이 있는 노선 12km 이용 시
            * 720((1250 - 350) * 0.8 ) + 900 + 100(10km 초과 ~ 50km 이하 - 5km 마다 100원) : 1720원

### 힌트

### /paths 요청시 LoginMember 객체 처리

* 로그인 시 LoginMember 객체를 활용하여 연령별 요금 할인 정책을 적용할 수 있음
* 비 로그인 시 LoginMember는 비어있는 객체가 넘어가므로 별도의 처리가 필요함
* 필요시 아래 구문에서 null Object를 리턴해 주는 부분을 예외로 던지도록 수정해도 무방함

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
public int calculateOverFare(int distance) {
  return (int) ((Math.ceil((distance -1) /5 ) + 1 ) * 100);
}
```

* ExtraChargePolicy
    * UnderTenChargePolicy
    * BetweenTenAndFiftyChargePolicy
    * OverFiftyChargePolicy
* DiscountPolicy
    * DefaultDiscountPolicy
    * ChildDiscountPolicy
    * TeenagerDiscountPolicy
