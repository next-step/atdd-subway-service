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


--- 
1단계 - 인수 테스트 기반 리팩터링

- [X] LineService 리팩터링
  - [X] LineService의 비즈니스 로직을 도메인으로 옮기기 
  - [X] domain의 단위 테스트를 작성하기
  
- [X] (선택) LineSectionAcceptanceTest 리팩터링
  - [X] API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
  
    ```text
      Feature: 지하철 구간 관련 기능
      Background:
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


* 참고 사항 
1. Domain으로 옮길 로직을 찾기
   스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
   서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
   기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   정상 동작 확인 후 기존 로직 제거


2단계 - 경로 조회 기능 

- [X] 최단 경로 조회 인수 테스트 만들기
- [X] 최단 경로 조회 기능 구현하기
    - 최단경로 가중치 : 거리 
      
    

- 요청 포멧 
  - Request 
    ```text
    HTTP/1.1 200 
    Request method:	GET
    Request URI:	http://localhost:55494/paths?source=1&target=6
    Headers: 	Accept=application/json
    Content-Type=application/json; charset=UTF-8
    
    ```
  - Response
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
  * 예외 사항
    * 출발역과 도착역이 같은 경우
    * 출발역과 도착역이 연결이 되어 있지 않은 경우
    * 의존재하지 않은 출발역이나 도착역을 조회 할 경우



--- 
3단계 인증을 통한 기능 구현


- 요구사항 
  - [X] 토큰 발급 기능 (로그인) 인수 테스트 만들기
    * 토큰 발급(로그인)을 검증하는 인수 테스트 만들기
    * AuthAcceptanceTest 인수 테스트 만들기

      - 인수 조건 
      ```text
       Feature: 로그인 기능
          Scenario: 로그인을 시도한다.
            Given: 회원 등록되어 있음
             When 로그인 요청
             Then 로그인 됨
      ```
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
  - 예외
    - 회원이 등록 되지 않은 경우 로그인 실패
    - 패스워드가 틀릴 경우 로그인 실패
    
- [X] 인증 - 내 정보 조회 기능 완성하기
    - [X] MemberAcceptanceTest 클래스의 manageMyInfo메서드에 인수 테스트를 추가하기
    - [X] 토큰을 통한 인증 구현
  
- [X] 인증 - 즐겨 찾기 기능 완성하기
  - 인수 조건
  ```text
  Feature: 즐겨찾기를 관리한다.

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음

    Scenario: 즐겨찾기를 관리
        When 즐겨찾기 생성을 요청 (2건)
        Then 즐겨찾기 생성됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회됨
        When 즐겨찾기 삭제할 즐겨찾기 조회
        Then 즐겨찾기 즐겨찾기가 조회 됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제됨
        WHEN 삭제된 즐겨 찾기 조회
        Then 삭제된 즐겨 찾기 조회되지 않음
  ```
  - 즐겨찾기 생성 요청/응답
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
  목록 조회 요청/응답
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
  - 삭제 요청 / 응답
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
4단계 요금조회

요구사항
- [ ] 경로 조회 시 거리 기준 요금 정보 포함하기
  - 거리별 요금 정책 
    - 기본운임(10㎞ 이내) : 기본운임 1,250원
    - 이용 거리초과 시 추가운임 부과
    - 10km초과∼50km까지(5km마다 100원)
    - 50km초과 시 (8km마다 100원)
  
- [ ] 노선별 추가 요금 정책 추가
  - 노선별 추가 요금 정책
    - 노선에 추가 요금 필드를 추가
    - 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가  
      ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원  
      ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
    - 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원
- [ ] 연령별 할인 정책 추가
  - 청소년: 운임에서 350원을 공제한 금액의 20%할인
  - 어린이: 운임에서 350원을 공제한 금액의 50%할인

- 인수 조건 
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