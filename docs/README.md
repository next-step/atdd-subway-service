## 인수 테스트 기반 TDD

### 1 단계 - 인수 테스트 기반 리팩터링 

#### 요구 사항 
- [ ] LineService 리팩터링
- [ ] LineSectionAcceptanceTest 리팩터링

##### 요구 사항 설명
- 인수 테스트 기반 리팩터링
  - [ ] LineService 의 비지니스 로직을 도메인으로 옮기기
  - [ ] 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링 하기
  - [ ] 전체 기능은 인수테스트로 보호 한뒤 세부 기능을 TDD로 리팩터링 하기

##### 구현 순서 
1. Domain 으로 옮길 로직을 찾기
   - 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
   - 객체지향 생활체조를 참고

2. Domain 의 단위 테스트를 작성하기 
   - 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
   - SectionTest 나 LineTest 클래스가 생성 될 수 있음

3. 로직을 옮기기
   - 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
   - 정상 동작 확인 후 기존 로직 제거 

##### 인수 테스트 통합
- API 를 검증하기 보다는 __시나리오, 흐름을 검증하는 테스트__로 리팩토링 하기
- 하나 이상의 시나리오가 필요한 경우 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음
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

------------
1. LineService 리팩토링 시작
    - 리팩토링 포인트 정리
      - [ ] Line 안의 Section List 를 일급 컬렉션 Sections 로 변경
      - [ ] LineService 에서 getStations 함수를 부분을 Sections 이동 
      - [ ] LineService 에서 구간 추가하는 부분을 Sections 으로 이동
      - [ ] LineService 에서 구각 삭제하는 부분을 Sections 으로 이동 



--------------
--------------

## 2단계 - 경로 조회 기능

#### 요구 사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

  - 요청 / 응답 포맷
    - Request
      ```http request
      HTTP/1.1 200
      Request method:	GET
      Request URI:	http://localhost:55494/paths?source=1&target=6
      Headers: 	Accept=application/json
                  Content-Type=application/json; charset=UTF-8
      ```
    - Response
      ```http request
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
       
- Class 역할 정리
  - PathController
    - ui 담당
  - PathService
    - StationRepository 를 통해서 입력으로 들어온 station id 를 확인한다.
    - Route utils 에 도움을 받아 최단 경로 와 거리합을 가져온다.
  - Route
    - jpgrapht library 를 사용하여 최단 경로와 거리를 계산한다.
  - Station
    - Section 정보를 가진다 (Line 과 Station 을 Section 을 중간 테이블로 두고 다대다 -> 일대다로 풀어서 정리)
  

----------------------
----------------------

## 3단계 - 인증을 통한 기능 구현

#### 요구 사항
- [x] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [x] 인증 - 내 정보 조회 기능 완성하기
- [x] 인증 - 즐겨 찾기 기능 완성하기 

- 인수 조건
```text
Feature: 로그인 기능

  Scenario: 로그인을 시도한다.
    Given 회원 등록되어 있음
    When 로그인 요청
    Then 로그인 됨
```

- 요청 / 응답
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
      ````text
            HTTP/1.1 200 
            Content-Type: application/json
            Transfer-Encoding: chunked
            Date: Sun, 27 Dec 2020 04:32:26 GMT
            Keep-Alive: timeout=60
            Connection: keep-alive
            
            {
                "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
            } 
      ````
- 인증 - 내정보 조회 기능
  - 인수 테스트
    1. 헤더의 JWT Token 정보를 인증해서 LoginMember 로 변환해서 테스트 성공 시키기
    2. @AuthenticationPrincipal 과 AuthenticationPrincipalArgumentResolver 를 활용해서 성공 시키기

- 즐겨 찾기 기능 구현 하기
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
          When 즐겨찾기 생성을 요청
          Then 즐겨찾기 생성됨
          When 즐겨찾기 목록 조회 요청
          Then 즐겨찾기 목록 조회됨
          When 즐겨찾기 삭제 요청
          Then 즐겨찾기 삭제됨
   ```
  - 생성 요청/응답
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
      
      HTTP/1.1 201 Created
      Keep-Alive: timeout=60
      Connection: keep-alive
      Content-Length: 0
      Date: Sun, 27 Dec 2020 04:32:26 GMT
      Location: /favorites/1
  ```
  - 목록 조회 요청 / 응답
  ```http request
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
  - 삭제 요청/응답
  ```http request
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
  
  - class 역할
    - FavoriteController
      - Request 와 Response 를 넘겨주는 역할 
    
    - FavoriteService
      - LoginMember id , FavoriteRequest 정보를 이용하여 FavoriteRepository 를 통해서 DB 에 저장
      - FavoriteResponse List 를 반환 
    
    - FavoriteRepository 
      - Long id, Long LoginMember_id, Station 가 OneToOne 관계인 source , OneToOne 관계인 destination
    

--------------
--------------

## 4단계 - 요금 조회

#### 요구사항
- [x] 경로 조회 시 거리 기준 요금 정보 포함하기
- [x] 노선별 추가 요금 정책 추가
- [x] 연령별 할인 정책 추가 

- 요구 사항 설명
  - 거리별 요금 정책
    - 기본운임(10km 이내) : 기본운임 1,250원 
    - 이용 거리 초과 시 추가운임 부과 
      - 10km ~ 50km (5km 100원)
      - 50km 초과시 (8km 마다 100원)
  - 수정된 인수 조건
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
  - Class 역할
    - Price 
      - VO class 이며 add, minus 메소드를 가진다.
      - add 메소드는 입력 받은 금액을 기존 금액과 합산하여 금액을 반환한다.
      - minus 메소드는 입력 받은 금액만큼 기존 금액에서 제외하고 금액을 반환한다.
      
    - Distance
      - calculate 메소드를 가지며, 입력받은 정책에 따라서 거리별에 따른 금액을 반환한다.

    - Sections
      - 구간들의 전체 길이를 알수 있다.
      - 구간에 대한 금액을 알수 있다.
    
    - Discount
      - VO class 이며 add, minus 메소드를 가진다.
      - 총 금액을 입력 받아서 할인률을 적용하여 나머지 금액을 반환한다.
  
    - ExceedCharge
      - 초과 거리와 금액 정보를 가지며, 거리를 입력받아서 초과 거리에 따른 초과 금액을 반환한다.
    
    - UserCost
      - 연령별 할인률과 총 요금을 입력 받아서 할인률을 적용후 나머지 금액을 반환한다.