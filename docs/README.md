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
- [ ] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

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
