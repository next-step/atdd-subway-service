## Step2 - 경로 조회 기능

### 미션 설명

#### 미션 요구사항

- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현

#### 요청 / 응답 포맷
##### REQUEST
```http request
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

##### RESPONSE
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

#### 최단 경로 라이브러리
- `jgrapht` 라이브러리를 사용해 최단 경로를 조회
- `정점(vertext)` 과 `간선(edge)`, 그리고 `가중치` 개념 이용
  - 정점 : 역
  - 간선 : 구간
  - 가중치 : 거리
  - [jgrapht-graph algorithm](https://jgrapht.org/guide/UserOverview#graph-algorithms)

#### 예외 상황
- 출발역과 도착역이 같은 경우
- 출발역과 도착역이 연결이 되어 있지 않은 경우
- 존재하지 않은 출발역이나 도착역을 조회 할 경우

### 인수 조건
#### - Feature: 최단 경로 조회
##### - BackGround:
```text
  - Given: 지하철 노선 "신분당선", "이호선", "삼호선" 이 등록되어 있음
  - And: 지하철 노선 "신분당선"에 지하철역 "강남역", "양재역"이 등록되어 있음
  - And: 지하철 노선 "이호선"에 지하철역 "교대역", "강남역"이 등록되어 있음
  - And: 지하철 노선 "삼호선"에 지하철역 "교대역", "남부터미널역"이 등록되어 있음
```
  
##### - Scenario: 최단 경로를 조회한다.
```text
  - When 최단 경로를 조회한다.
  - Then 최단 경로를 응답한다.
```

##### - Scenario: 최단 경로 조회를 실패한다 - 출발역과 도착역이 같은 경우
```text
  - When 동일한 출발역과 도착역으로 최단 경로를 조회한다.
  - Then 최단 경로를 응답을 실패한다.
```

##### - Scenario: 최단 경로 조회를 실패한다 - 출발역과 도착역이 연결이 되어 있지 않은 경우
```text
  - When 출발역과 도착역이 연결이 되어 있지 않은 최단 경로를 조회한다.
  - Then 최단 경로를 응답을 실패한다.
```

##### - Scenario: 최단 경로 조회를 실패한다 - 존재하지 않은 출발역이나 도착역을 조회 할 경우
```text
  - When 존재하지 않은 출발역이나 도착역을 최단 경로를 조회한다.
  - Then 최단 경로를 응답을 실패한다.
```

### 구현 기능 목록
- [x] 최단 경로 조회 인수 테스트 작성
  - [x] 조회 성공
  - [x] 조회 실패
    - 출발역과 도착역이 같은 경우
    - 출발역과 도착역이 연결이 되어 있지 않은 경우
    - 존재하지 않은 출발역이나 도착역을 조회 할 경우
- [ ] 최단 경로 조회 기능 구현
  - [x] 도메인 테스트 작성
  - [ ] 서비스 테스트 작성
  - [ ] 기능 구현

##  