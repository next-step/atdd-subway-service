# 인수테스트 기반 TDD
## STEP3 - 경로 조회 기능
### 요구사항
- [ ] 토큰 발급 기능 (로그인) 인수 테스트 만들기
- [ ] 인증 - 내 정보 조회 기능 완성하기
- [ ] 인증 - 즐겨 찾기 기능 완성하기

#### 세부 요구 사항
- [ ] 이메일과 패스워드를 이용하여 요청 시 access token을 응답하는 기능을 구현하기 
- [ ] AuthAcceptanceTest을 만족하도록 구현하면 됨
- [ ] AuthAcceptanceTest에서 제시하는 예외 케이스도 함께 고려하여 구현하기 
- [ ] Bearer Auth 유효하지 않은 토큰 인수 테스트
- [ ] 유효하지 않은 토큰으로 /members/me 요청을 보낼 경우에 대한 예외 처리

### 인수 테스트 시나리오
- [ ] Feature: 로그인 기능
  - [ ] Scenario: 로그인을 시도한다.
    - Given 회원 등록되어 있음
    - [ ] When 유효하지 않은 아이디와 비밀번호로 로그인 요청
    - Then 유효하지 않은 요청 예외 발생
    - [ ] When 로그인 요청
    - Then 로그인 됨
    - Then accessToken이 반환됨
    - [ ] When 전달받은 accessToken이 아닌 다른 토큰으로 요청
    - Then 유효하지 않은 토큰 예외 발생
      
- [ ] Feature: 즐겨찾기를 관리한다.
  - [ ] Background
    - Given 지하철역 등록되어 있음
    - And 지하철 노선 등록되어 있음
    - And 지하철 노선에 지하철역 등록되어 있음
    - And 회원 등록되어 있음
    - And 로그인 되어있음
  - [ ] Scenario: 즐겨찾기를 관리
    - When 즐겨찾기 생성을 요청
    - Then 즐겨찾기 생성됨
    - When 즐겨찾기 목록 조회 요청
    - Then 즐겨찾기 목록 조회됨
    - When 즐겨찾기 삭제 요청
    - Then 즐겨찾기 삭제됨

### 기능 구현 목록
- [ ] 로그인과 토큰 검증
  - [ ] 토큰 발급 인수테스트 구현
  - [ ] 유효 하지 않은 토큰으로 요청 인수테스트 구현
  
- [ ] 즐겨찾기 기능 구현
  - [ ] 인수테스트 구현
    - [ ] 즐겨찾기 생성 테스트 구현
    - [ ] 즐겨찾기 조회 구현
    - [ ] 즐겨찾기 삭제 구현
    
  - [ ] 기능 구현
    - [ ] 즐겨찾기 생성 테스트 구현
    - [ ] 즐겨찾기 조회 구현
    - [ ] 즐겨찾기 삭제 구현   

  - [ ] 리팩토링
    - [ ] 즐겨찾기 생성 테스트 구현
    - [ ] 즐겨찾기 조회 구현
    - [ ] 즐겨찾기 삭제 구현


## STEP2 - 경로 조회 기능
### 요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

### 요청 / 응답 포맷
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
### 예외 상황 예시
* 출발역과 도착역이 같은 경우
* 출발역과 도착역이 연결이 되어 있지 않은 경우
* 존재하지 않은 출발역이나 도착역을 조회할 경우
  
### 인수 테스트 시나리오
- Feature: 경로 조회 기능
  - Background
    - given: 양평역이 등록되어있음
    - and: 영등포구청역이 등록되어있음
    - and: 영등포시장역이 등록되어있음
    - and: 신길역이 등록되어있음
    - and: 여의도역이 등록되어있음
    - and: 당산역이 등록되어있음
    - and: 영등포역이 등록되어있음
    - and: 5호선 양평역 - 10 - 영등포구청역 - 5 - 영등포시장역 - 10 - 신길역 - 5 - 여의도역 노선이 등록되어 있음
    - and: 2호선 영등포구청역 - 10 - 당산역 노선이 등록되어 있음
    - and: 1호선 신길역 - 5 - 영등포역 노선이 등록되어 있음
  - [x] Scenario: 경로 조회
    - when: 출발역 영등포구청역 도착역 신길역을 경로를 조회한다(같은 라인)
    - then: 경로가 조회 된다.
    - then: 경유 지하철역은 양평역, 영등포구청역, 영등포시장역이다.
    - then: 이동 거리는 20이다.
    - when: 출발역 교대역 당산역 양평역의 경로를 조회한다(1회 환승)
    - then: 경로가 조회 된다.
    - then: 경유 지하철역은 당산역, 영등포구청역, 양평역이다.
    - then: 이동 거리는 20이다.
    - when: 출발역 영등포역 도착역 당산역의 경로를 조회한다(2회 환승)
    - then: 경로가 조회 된다.
    - then: 경유 지하철역은 영등포역, 신길역, 영등포시장역, 영등포구청역, 당산역이다.
    - then: 이동 거리는 30 이다.
  - [x] Scenario: 경로 조회 실패
    - given: 야탑역, 모란역이 등록되어 있음.
    - and: 신분당선이 야탑역 - 10 - 모란역이 등록되어 있음.
    - when: 출발역 영등포구청역 도착역 영등포구청역의 경로를 조회한다
    - then: 경로 조회가 실패한다.(출발역과 도착역이 같은 경우)
    - when: 출발역 대구역, 도착역 양평역의 경로를 조회한다.
    - then: 경로 조회가 실패한다.(존재하지 않은 출발역이나 도착역을 조회할 경우)
    - when: 출발역 양평역, 도착역 대구역의 경로를 조회한다.
    - then: 경로 조회가 실패한다.(존재하지 않은 출발역이나 도착역을 조회할 경우)
    - when: 출발역 영등포구청역, 도착역 모란역의 경로를 조회한다.
    - then: 경로 조회가 실패한다.(출발역과 도착역이 연결이 되어 있지 않은 경우)

### 기능 구현 목록
- [X] PathController 구현
- [X] PathService에 경로 조회 기능 구현
  - [X] SectionService에서 구간 전체 조회(mock)
  - [x] StationService에서 출발 역 조회(mock)
  - [x] StationService에서 도착 역 조회(mock)
  - [x] source, target 기준으로 경로 조회 기능(PathFinder)
- [x] PathFinder 구현
  - [X] 테스트 구현

### 피드백 반영 필요 목록
- [x] 역 정보 주석 그리기
- [X] 경로 조회시 불 필요한 초기화 제거, TestInstance.Lifecycle.PER_CLASS 적용
- [x] PathController의 findPaths 반환 타입을 ResponseEntity로 변경
- [x] PathService의 역 조회 부분 한번에 초기화
- [x] PathGraph 내 코드 구조 개선
- [x] PathServiceTest, PathFinderTest에 TestFixture 적용


## STEP1 - ATDD 기반 리팩토링
### 요구사항
- [x] LineSectionAcceptanceTest 리팩터링
- [x] LineService 리팩터링

### 기능 구현 목록
- [x] LineSectionAcceptanceTest 리팩터링
  - [x] TestFactory 사용
  
- [x] LineService 리팩토링
  - [x] Service 내에 orElseThrow 메소드가 던지는 예외가 RuntimeException인 경우 EntityNotFound로 대체
  - [x] getStations 도메인 추출 및 리팩토링
    - [x] Line 도메인 내 sections를 1급 콜렉션으로 대체
    - [x] Line 도메인 내 getStations 메소드 구현
    - [x] getStations 메소드가 반환하는 List<Station>을 1급 콜렉션인 Stations 구현 및 대체
  - [x] 도메인 to DTO 메소드 구현
    - [x] LineResponse에 List<Line> to List<LineResponse> 메소드 구현
    - [x] LineResponse에 Line to LineResponse 메소드 구현
    - [x] StationResponse에 Stations to List<StationResponse> 메소드 구현
    - [x] StationResponse에 Line to StationResponse 메소드 구현
  - [x] findUpStation 도메인 추출 및 리팩토링
  - [x] SaveLine 리팩토링
    - [x] ***도메인 추출*** LineRequest의 도메인 프로세스 전환 메소드 구현
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [x] fineLines 리팩토링
    - [x] ***도메인 추출*** LineResponse 내 팩토리 메소드로 Line을 변환 후 반환
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환
  - [x] findLineResponseById 리팩토링
    - [x] ***도메인 추출*** 특정 노선에 속한 역을 등록된 구간을 기준으로 정렬하여 반환링
  - [x] updateLine 리팩토링
    - [x] Line 도메인 내에 update하는 메소드 구현 후 적용
  - [x] ***도메인 추출*** addLineStation 리팩토링
  - [x] ***도메인 추출*** removeLineStation 리팩토링
  
### 피드백 반영 필요 목록
- [x] 전체적인 시나리오 검증 테스트 구현
- [x] getConnectedStationCount() 메소드의 반환 값을 int타입으로 대체 
- [x] 읽기 전용 조회 서비스 로직에는 @Trasactional(readOnly = true) 옵션 적용
- [x] Sections 내에 Optional과 isPresent()를 사용하는 부분을 ifPresent로 대체
- [x] e.printStackTrace 대신 logback으로 처리
- [x] SectionsTest의 역 조회 검증 로직을 size() 뿐만 아니라 세부 요소까지 확인하도록 개선