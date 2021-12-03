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