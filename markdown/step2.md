
##요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기

###수행순서
- 1 PathAcceptanceTest 작성
  - 1-1 dto.PathResponse 생성
- 2 Mock데이터를 응답하는 PathController 작성
  - 2-1 빈 메서드를 가진 협력객체 PathService 생성
- 3 PathServiceTest 생성
  - 3-1 pathService의 빈메서드에 대한 기능 테스트 작성
- 4 PathService 테스트통과되도록 수정
  - 4-1 PathService 리팩토링
  - 4-2 PathFinder 생성, 역할위임
- 5 PathFinder 리팩토링

###질문
1. 수행순서중 4-1 리팩토링 과정중 PathFinder로 역할을 위임. 테스트는 언제 작성해야할까?
2. 인수테스트에서 예외사항 작성, SeriveTest에도 예외사항을 다 작성해야 할까?

###예외사항
- [x] 출발역과 도착역이 같은 경우
- [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
- [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우

###!!!!
TDD의 방향보다 테스트를 통해 구현할 기능을 명세하는것과 리팩터링이 더 중요합니다!

두 방향성을 모두 사용해보시고 테스트가 협력 객체의 세부 구현에 의존하는 경우(가짜 협력 객체 사용)와 테스트 대상이 협력 객체와 독립적이지 못하고 변경에 영향을 받는 경우(실제 협력 객체 사용)를 모두 경험해보세요 :)


###요청/응답포맷
####request
~~~
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8
~~~

####response
~~~
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
~~~