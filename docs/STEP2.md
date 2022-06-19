## 1단계 - 인수 테스트 기반 리팩터링

### 요구사항
- [ ] 최단 경로 조회 인수 테스트 만들기
- [ ] 최단 경로 조회 기능 구현하기

### 요청/응답 포맷
* Request
```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

* Response
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

### 요구사항 구현
- [ ] 최단 경로 조회 인수 테스트 작성(요구사항과 기능 전반에 대한 이해)
  - [x] PathAcceptanceTest 작성
  - [x] PathController 레이어 구현
  - [x] PathService 레이어 테스트 작성
  - [ ] PathService 레이어 기능 구현
- [ ] 경로 조회 도메인 구현
  - [x] Path 도메인 구현
  - [x] PathResponse 도메인 구현
  - [x] PathFinder 도메인 테스트 작성
  - [ ] PathFinder 도메인 기능 구현