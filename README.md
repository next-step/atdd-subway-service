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

## 강의내용 간략 정리
### 단위 테스트와 TDD
단위 테스트를 딱 잘라 정의하긴 어렵지만,  
`협력 객체`에 따라서 구분할 수 있음

> 단위 테스트
> - 작은 코드 조각(단위)을 검증
> - 빠르게 수행 가능
> - **격리된 방식**으로 처리

'격리해야 하는 대상을 무엇으로 보느냐?' 에서 차이가 발생

#### Classist(테스트 자체를 격리) ; 통합
- 검증해야 하는 `테스트(기능)`과 다른 `테스트(기능)`을 격리
- 따라서 테스트간 공유하는 의존성이 아니라면 `실제 객체`를 사용

#### Mockist(테스트 해야하는 객체를 격리) ; 고립
- 검증해야 하는 `테스트 대상(객체)`와 다른 `협력 객체`를 격리
- 따라서 테스트 대상(객체)이 의존하는 것을 `가짜 객체`로 대체

#### TDD 흐름의 종류
위에서 정의한 Classist 와 Mockist 에 따라 각각  
Inside Out vs Outside In 으로 TDD의 흐름을 정의할 수 있다.
##### Inside Out (Classist - Chicago style)
- 실제 객체를 다뤄야 하기 때문에 도메인 모델부터 시작
##### Outside In (Mockist - London style)
- 상위 레벨부터 시작
- 테스트 더블을 적극 활용하여 협력 객체의 예상 결과를 정의
- 다음 사이클로 테스트 더블로 미리 정의한 협력 객체를 테스트 대상

#### 정리
- **_정답은 없다_** : 상향식과 하향식을 적절히 섞어서 아는것에서 모르는 방향으로 전개  
- Top-Down 으로 방향을 잡고, Bottom-up 구현
- 내부 구현에 대한 설계 흐름을 구상
- 설계가 끝나면 도메인부터 차근차근 TDD로 기능 구현
- **길 잃으면 인수테스트로 돌아와서 다시 길을 볼 것**
- TDD를 연습할때는 실제 객체를 활용하는 것을 우선
- 영 안되겠다 싶으면 테스트 더블(Mocking 등등..) 활용

### 인수 테스트 기반 리팩토링
비즈니스 로직을 리팩터링 할 때 무엇부터 해야하는지? 에서 부터 시작  
- **테스트 코드** 먼저 수정
- 생각해보면, TDD 라는 것 자체가 테스트 코드부터 시작하니 리팩터링도 테스트 코드부터 하는게 당연

#### 구체적인 리펙터링 프로세스(스트랭글러 패턴 ; Strangle pattern)
new 로 교체하고, legacy 는 일정 기간 후 삭제하는 작업 패턴(**인수 테스트 보우하사 리펙터링 만세**)
1. 인수 테스트를 작성하여 리펙터링의 명확한 길을 잡는다
2. 새로운 코드를 한벌 더 만든다(가능한 수준 만큼 제한은 없음)
3. 파악이 가능한 부분부터 단위 테스트를 만들어 기능 검증하기
4. 기능 구현 후 **기존 코드 제거**하여 새로운 인수 테스트와 **기존 코드를 잘 대체하는 지** 확인

## 이번 미션에서의 목표
- 인수 테스트에서 TDD 사이클로 넘어가는 흐름 경험

### 개인적 목표
- 테스트 더블 사용법에 대한 숙지(Mock 등등..)

## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항
- [x] LineSectionAcceptanceTest 리팩터링 ; 인수 테스트 통합 작성 완료
- [x] LineService 리팩터링
    - LineService의 비즈니스 로직을 도메인으로 옮기기
    - 부분 부분 리팩터링
    - 인수테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링

#### 요구사항 설명
1. Domain 으로 옮길 로직을 찾기
   - **스프링 빈을 사용하는 객체**는 제외
   - **스프링 빈을 사용하는 객체에 의존하는 로직**은 제외
   - 나머지 로직을 도메인으로!!
   - 객체지향 생활체조 원칙 참고
2. Domain 단위 테스트를 작성하기
   - 1 단계에서 옮겨 올 로직의 기능을 테스트
   - SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거

### 1단계 피드백 기능 목록
- [x] 기능 목록 작성 시 인수 조건, 도메인 기능 목록 등 상세화
- [x] Service에서 Service를 의존하면 순환참조 발생 가능 --> 방향은 아래로 흐르는 것이 좋음
- [x] 조회 메서드에 `@Transactional(readOnly=true)` 선언하여, `Lazy Loading` 대비
- [x] 매직넘버에 이름 부여

## 2단계 - 경로 조회 기능

### Request / Response
```http request
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

```http response
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


```text
     교대역    --- 2호선 : 10 ---   강남역
     |                             |
    3호선 : 3                     신분당선 : 10
     |                             |
     남부터미널역  --- 3호선 : 2 ---   양재
```
### 인수 테스트
- [x] 최단 경로 조회 인수 테스트 만들기
  - Given : 경로 조회 가능한 노선들이 등록되어 있다
  - When : 출발역과 도착역의 경로 조회 요청하면
  - Then : 최단 경로와 거리를 응답한다.

### Domain
- [x] PathFinder 구현
  - [x] 최단 경로 찾기
  - [x] 예외 케이스 구현
    - [x] 출발역과 도착역이 같은 경우
    - [x] 출발역과 도착역이 연결되어 있지 않은 경우
    - [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우

### Repository
- [x] SectionRepository 구현
  - [x] EntityGraph 로 join 쿼리로 조회 

### Service
- [x] PathService 구현

### 2단계 피드백
- [x] 매직넘버에 대해서 책임을 enum으로 분리 vs 해당 객체에 위임 고민
  - 확장성을 고려한다면? --> enum으로 분리
  - 객체의 책임에 집중한다면? --> 멤버 변수로

## 3단계 - 인증을 통한 기능 구현

### 기능 목록 작성
- [x] 토큰 발급 기능(로그인) 인수 테스트
  - 토큰 발급 정상 케이스에 대한 인수 테스트 만들기
    ```text
    Feature: 로그인 기능
    
      Scenario: 로그인을 시도한다.
          Given 회원 등록되어 있음
          When 로그인 요청
          Then 로그인 됨
    ```
  - 예외 케이스에 대한 기능 구현
    - Email 이 유효하지 않은 경우
    - password가 유효하지 않은 경우
    - secret key 등 jwt 생성 시 필요조건이 충족되지 않은 경우

#### 토근관련 Request / Response
```http request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

```http response
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

- [x] 내 정보 조회 기능
  - `MemberAcceptanceTest` 클래스의 `manageMyInfo`메서드에 인수 테스트를 추가하기
  - 내 정보 조회, 수정, 삭제 기능을 `/members/me` 라는 URI 요청으로 동작하도록 검증
  - 로그인 후 발급받은 토큰을 포함해서 요청하기
    - `@AuthenticationPrincipal`과 `AuthenticationPrincipalArgumentResolver`을 활용하기
    - 조회 / 수정 / 삭제 기능이 토큰을 통해 확인된 member에 한해서 잘 동작하도록 구현
  ```java
  @DisplayName("나의 정보를 관리한다.")
  @Test
  void manageMyInfo() {
    // implement
  }
  ```
- [x] 즐겨찾기 기능 구현하기
  - 즐겨찾기 기능을 완성하기
  - 인증을 포함하여 전체 ATDD 사이클을 경험할 수 있도록 기능을 구현하기
  ```text
  // 즐겨찾기 관련 기능 인수 테스트 통합
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

#### 즐겨찾기 관련 Request / Response
- 즐겨찾기 생성 요청 / 응답
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
```http resonse
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Sun, 27 Dec 2020 04:32:26 GMT
Location: /favorites/1
```

- 즐겨찾기 목록 조회 요청
```http request
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: application/json
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```
```http response
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

- 즐겨찾기 삭제 요청 / 응답
```http request
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY
accept: */*
host: localhost:50336
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/14.0.2)
accept-encoding: gzip,deflate
```
```http response
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Sun, 27 Dec 2020 04:32:26 GMT
```

### 인증 기반 인수 테스트 참고
- 사용자 정보를 인수 테스트 메서드의 첫번째 파라미터로 넘겨줄 수 있음
```java
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

### 참고) 코틀린의 확장함수
클래스에 상속하거나 디자인 패턴등의 방법을 사용하지 않고,  
새로운 기능으로 클래스를 확장 할 수 있는 기능
- 예시
```kotlin
// MutableList<Int> 타입은 앞으로 swap이란 함수를 사용할 수 있다.
fun MutableList<Int>.swap(index1: Int, index2: Int){
	val tmp = this[index1] 	// 'this'의 경우 MutableList에 해당한다.
	this[index1] = this[index2]
	this[index2] = tmp
}
```

### 3단계 피드백
- [x] Favorite은 member를 필수로 가지므로, 생성자의 parameter로 member 추가
- Q) member --> favorite 의존성이 설정한 이유?
  - A) `getFavorite()`은 accessToken 으로 인증된 멤버의 즐겨찾기를 가져오는 기능이다.
    따라서, member와 favorite의 객체간 연관관계의 객체 그래프를 통해 조회하는 것이 좋은 방법이라고 생각함  
    이게 아니라면, favoriteRepository에서 memberId로 조회를 하거나 모든 favorite을 가져와서,
    memberId로 필터링 하는 수 밖에 없을텐데, 이는 데이터 중심 위주인 것 같아 member --> favorite의 의존성 추가

## 4단계 - 요금 조회
- [x] Line에 `추가 요금` column 추가
  - [x] 도메인 테스트 수정 및 추가
  - [x] 인수 테스트 수정 및 추가
- [x] 경로 조회는 반드시 로그인이 필요로 하도록 수정
- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
  - [x] 운임료 계산하는 역할을 담당하는 객체를 분리하면? --> 운임료 계산기(FareCalculator)
    - Parameter List
      - 경로 결과(Path)
        - 경로의 역 list
        - 총 구간 길이
      - 모든 구간 리스트(All Sections)
        - 추가 운임 확인용(경로의 역의 노선을 확인하여 추가 운임 까지 객체 그래프 탐색 용도)
      - 할인정책에 대한 interface 포함...(멤버 변수)

### 운임료 계산 주요 로직 정리
- [x] 이용 거리 마다 추가 운임 부과
  - 10km 이내 : 1,250원
  - 10km 초과 ~ 50km 이내 : 5km 마다 100원
  - 50km 이상 : 8km 마다 100원
- [ ] 노선별 추가 요금 부과
  - 노선의 추가 요금을 확인하여 가장 금액이 큰 추가요금을 일괄 부과한다.  
    ex) 신분당선 : 900원, 분당선 : 400원 일 경우,  
      신분당과 분당선에 걸쳐있는 경로라면, **가장 비싼 신분당의 초과 요금(900원)만 부과**
- [ ] 연령 할인 정책 적용
  - 청소년 : 13세 이상 ~ 19세 미만 --> 운임에서 350원 공제(빼기)한 금액의 20% 할인
  - 어린이 : 6세 이상 ~ 13세 미만 --> 운임에서 350원 공제(빼기)한 금액의 50% 할인