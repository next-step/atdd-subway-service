# 5주차 STEP2_경로_조회_기능

## 🚀 요구사항
### 1. 최단 경로 조회 인수 테스트 만들기
- LinesTest
- PathServiceTest
- 인수 테스트 픽스쳐 (PathAcceptanceTest)
```
Feature: 지하철 경로 조회
  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음 (신분당선, 이호선, 삼호선)
    And 지하철 노선에 지하철역 등록 요청 (삼호선에 지하철역 추가)
    
  Scenario: 최단 경로 조회
    When 최단 경로 조회 요청
    Then 최단 경로 거리 비교
    
  Scenario: 노선에 등록되지 않은 역 최단 경로 조회시 에러 발생
    When 최단 경로 조회 요청
    Then 404 에러 발생
```

### 2. 최단 경로 조회 기능 구현하기
- application
  - PathService
- domain
  - Path, PathFinder, Lines
- dto
  - PathResponse

- Request
```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

- Response
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
