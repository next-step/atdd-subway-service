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

## 1단계 - 인수 테스트 기반 리펙토링
### 요구사항
* LineSectionAcceptanceTest 리팩터링
* LineService 리팩터링
### 요구사항 설명
* API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
* 반드시 하나의 시라니오로 통합할 필요는 없음, 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 신수 테스트를 작성할 수 있음
### 인수 테스트 기반 리펙토링
* LineService의 비즈니스 로직을 도메인으로 옮기기
* 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
* 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기

1. Domain으로 옮길 로직을 찾기
* 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
* 객체지향 생활체조를 참고

2. Domain의 단위 테스트를 작성하기
* 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
* SectionsTest나 LineTest 클래스가 생성될 수 있음

3. 로직을 옮기기
* 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
* 정상 동작 확인 후 기존 로직 제거

## 인수 테스트 시나리오
* Feature: 지하철 구간 관련 기능
    * Background
        * given: 강남역, 양재역, 정자역, 광교역 지하철 역이 등록되어 있음
        * and: 강남역 - 광교역 구간으로 신분당선이 등록되어 있음 
        
    * [x] Scenario: 지하철 구간 추가 및 조회 기능
        * whne: 정자역, 양재역 구간을 등록 요청한다.
        * then: 등록되지 않은 역구간으로 인해 등록에 실패한다.
        * when: 신분당선 구간에 강남역, 양재역 구간을 등록 요청 한다.
        * then: 신분당선 구간에 강남역, 양재역 구간이 등록된다.
        * when: 신분당선 구간에 정자역, 강남역 구간을 등록 요청 한다.
        * then: 신분당선 구간에 정자역, 강남역 구간이 등록된다.
        * when: 신분당선 구간에 정자역, 강남역 구간을 등록 요청 한다.
        * then: 이미 등록된 구간이라 등록에 실패한다.
        * when: 지하철 노선을 조회한다.
        * then: 지하철 노선에 지하철역이 강남역, 양재역, 광교역 순서로 정렬되어 있다.
        
    * [x] Scenario: 지하철 구간 삭제 기능
        * given: 신분당선 구간에 강남역 - 양재역 - 광교역 구간이 등록되어 있다.
        * when: 강남역을 노선에서 제외 요청 한다.
        * then: 강남역이 지하철역이 노선에서 제외된다.
        * when: 양재역을 노선에서 제외 요청 한다.
        * then: 양재역이 노선에서 제외가 실패한다.
  
## 기능 구현 목록
* [x] 작성한 인수테스트 시나리오 기반으로 인수테스트 코드 리팩터링
    * [x] 지하철 구간 추가 및 조회 기능 인수테스트 코드 리펙터링
    * [x] 지하철 구간 삭제 기능 인수테스트 코드 리펙터링

* [x] LineService 리팩터링
    * [x] Domain으로 옮길 로직을 찾기
        - Line
        - LineRequest
        - LineResponse
        - Sections 객체 신규 생성
    * [x] Domain 객체 단위 테스트 코드 작성
        * [x] LineRequest
            * [x] Station(up, down) 정보를 이용하여 Line entity 생성
            * [x] name, color 정보를 이용하여 Line entity 생성
        * [x] Line
            * [x] Line 객체를 이용하여 stations 정보 생성
            * [x] Line 객체 update
            * [x] Line에 section 추가
            * [x] Line에 section 제거
        * [x] LineResponse
            * [x] Line 객체를 이용하여 LineResponse 객체 생성
        * [x] SectionRequest
            * SectionRequest를 이용하여 Section 객체 생성
        * [x] Sections 일급 컬렉션 객체 생성
            * [x] addSection: 구간 정보 추가 기능
            * [x] removeSection: 구간 제거 기능
            * [x] stations: 상행에서 하행으로 정렬된 지하철역 리스트 반환
    * [x] LineService에 domain 로직 옮기기

## 2단계 - 경로 조회 기능
### 요구사항
* 최단 경로 조회 인수 테스트 만들기
* 최단 경로 조회 기능 구현하기

#### 요청 / 응답 포맷
request
```http request
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```
response
````http request
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
````
#### 예외 상황 예시
* 출발역과 도착역이 같은 경우
* 출발역과 도착역이 연결이 되어 있지 않은 경우
* 존재하지 않은 출발역이나 도착역을 조회할 경우

## 인수 테스트 시나리오
* Feature: 경로 조회 기능
    * Background
        * given: 고속버스터미널역이 등록되어 있음
        * and: 교대역이 등록되어 있음
        * and: 남부터미널역이 등록되어 있음
        * and: 양재역이 등록되어 있음
        * and: 강남역이 등록되어 있음
        * and: 서초역이 등록되어 있음
        * and: 역삼역이 등록되어 있음
        * and: 양재시민의숲역이 등록되어 있음
        * and: 2호선 서초-10-교대-10-강남-5-역삼역 노선이 등록되어 있음
        * and: 3호선 고속버스터미널-3-교대-4-남부터미널-5-양재역 노선이 등록되어 있음
        * and: 신분당선 강남역-15-양재역-12-양재시민의숲역이등록되어 있음
        
    * [x] Scenario: 경로 조회
        * when: 출발역 강남역 도착역 서초역의 경로를 조회한다(같은 라인)
        * then: 경로가 조회 된다.
        * then: 경유 지하철역은 강남역, 교대역, 서초역이다.
        * then: 이동 거리는 20이다.
        * when: 출발역 교대역 도착역 양재시민의숲역의 경로를 조회한다(1회 환승)
        * then: 경로가 조회 된다.
        * then: 경유 지하철역은 교대역, 남부터미널역, 양재역, 양재시민의숲역이다.
        * then: 이동 거리는 21이다.
        * when: 출발역 서초역 도착역 양재시민의숲역의 경로를 조회한다(2회 환승)
        * then: 경로가 조회 된다.
        * then: 경유 지하철역은 서초역, 교대역, 남부터미널역, 양재역, 양재시민의숲역이다.
        * then: 이동 거리는 31이다.
        
    * [x] Scenario: 경로 조회 실패
        * given: 사당역, 과천역이 등록되어 있음.
        * and: 4호선이 사당역-10-과천역이 등록되어 있음.
        * when: 출발역 강남역 도착역 강남역의 경로를 조회한다
        * then: 경로 조회가 실패한다.(출발역과 도착역이 같은 경우)
        * when: 출발역 교대역, 도착역 압구정역의 경로를 조회한다.
        * then: 경로 조회가 실패한다.(존재하지 않은 출발역이나 도착역을 조회할 경우)
        * when: 출발역 압구정역, 도착역 교대역의 경로를 조회한다.
        * then: 경로 조회가 실패한다.(존재하지 않은 출발역이나 도착역을 조회할 경우)
        * when: 출발역 강남역, 도착역 과천역의 경로를 조회한다.
        * then: 경로 조회가 실패한다.(출발역과 도착역이 연결이 되어 있지 않은 경우)
## 기능 구현 목록
* [x] 작성한 인수테스트 시나리오 기반으로 인수테스트 작성
* [x] outside In 방식으로 경로 조회 TDD
    * 전체 지하철 노선 조회 기능(lineRepository mock)
    * source 기준 지하철역 정보 조회 기능(StationRepository mock)
    * target 기준 지하철역 정보 조회 기능(StationRepository mock)
    * 노선, source, target 기준으로 경로 조회 기능(PathFinder mock)
* [x] inside Out 방식으로 경로 조회 TDD
    * pathFinder component 구현

* [x] 경로 조회 실패 처리 기능 개발 TDD
    * 단위 테스트 작성을 통하여 경로 조회 실패 케이스 기능 구현
        * [x] 존재하지 않은 출발역이나 도착역을 조회할 경우
        * [x] 출발역과 도착역이 같은 경우
        * [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
