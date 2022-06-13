## 요구사항

* [x] LineService 리팩토링
* [x] LineSectionAcceptanceTest 리팩토링
* [ ] 최단 경로 조회 인수 테스트 만들기
* [ ] 최단 경로 조회 기능 구현하기

## 요구사항 설명

* 인수 테스트 기반 리펙토링
    * LineService 의 비즈니스 로직을 도메인으로 옮기기
    * 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
    * 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD 로 리팩터링하기
* 다음과 같은 순서로 진행한다
    1. Domain 으로 옮길 로직 찾기
       * 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고 도메인으로 옮긴다
       * [개체지향 생활체조 참고](https://developerfarm.wordpress.com/2012/02/03/object_calisthenics_summary/)
    2. Domain 의 단위 테스트를 작성
       * 서비스 레이어에서 옮겨 올 로직의 기능 테스트
       * SectionsTest 나 LineTest 클래스가 생성될 수 있다
    3. 로직 옮기기
       * 기존 로직을 지우지 말고 새로운 로직을 만들어서 수행
       * 정상 동작 확인 후 기존 로직을 제거한다

## 미션 수행 순서

* 최단 경로 조회
    1. mock 서버와 dto 를 정의하여 인수 테스트 성공 시키기

## 예외상황

* 최단 경로 조회 예외상황
    1. 출발역과 도착역이 같지 않은 경우
    2. 출발역과 도착역이 연결이 되어 있지 않은 경우
    3. 존재하지 않은 출발역이나 도착역을 조회하는 경우

## 인수 테스트 통합

* API 를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
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

### 외부 라이브러리 테스트

* 외부 라이브러리의 구현은 수정할 수 없기 때문에 단위 테스트를 하지 않는다
* 외부 라이브러리를 사용하여 직접 구현하는 로직을 검증해야 한다
* 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용한다

### 최단 경로 조회 요청/응답

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
