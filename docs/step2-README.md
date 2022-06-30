# 2단계-경로 조회 기능

### 요구사항
- 최단 경로 조회 인수 테스트 만들기
- 최단 경로 조회 기능 구현하기

### 요청/응답 포맷
Request
```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```
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

### 힌트
최단 경로 라이브러리
- jgrapht 라이브러리를 활용하면 간편하게 최단거리를 조회할 수 있음
- 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
  - 정점: 지하철역(Station)
  - 간선: 지하철역 연결정보(Section)
  - 가중치: 거리
- 최단 거리 기준 조회 시 가중치를 거리로 설정

```
@Test
public void getDijkstraShortestPath() {
    WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
    List<String> shortestPath 
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

    assertThat(shortestPath.size()).isEqualTo(3);
}
```

---
### TODO List
지하철 경로 조회 기능의 인수테스트 작성
  - [x] 지하철 최단 경로를 조회한다.
  - [x] 출발역과 도착역이 같은 경우, 최단 경로를 조회할 수 없다.
  - [x] 출발역과 도착역이 연결되어 있지 않은 경우, 최단 경로를 조회할 수 없다.
  - [x] 출발역이나 도착역이 존재하지 않는 경우, 최단 경로를 조회할 수 없다.
  
TDD를 기반으로 지하철 경로 조회 기능 구현
  - Service계층과 Domain계층에 대한 단위 테스트를 작성한다.
    - [x] 전체 노선에서 출발지로부터 도착지까지의 최단 경로 조회
      - [x] 출발지와 도착지가 같은 경우 예외
      - [x] 출발지와 도착지 사이에 간선이 없는 경우 예외
      - [x] 출발지 혹은 도착지가 존재하지 않는 경우 예외
---
### 코드 리뷰 피드백 내용 정리
- [x] 엔티티를 응답 객체로 반환하는 경우 발생할 수 있는 문제
  - 데이터베이스의 테이블과 엔티티를 반환하는 경우 테이블 명세가 외부로 노출
  - 응답 항목이 추가/변경 되는 요구사항에 테이블과 매핑된 엔티티로는 유연하게 대처할 수 없음
  - 해당 엔티티가 양방향 연관관계를 가지고, toString이 구현되어 있는 경우 순환 참조로 인해 `StackOverFlow ` 발생할 수 있음에 주의
  - FetchType이 Lazy로 설정된 다른 엔티티와 연관관계를 가지는 경우, 의도치 않은 `N+1` 문제가 발생할 수 있음
  - FetchType이 Lazy로 설정된 다른 엔티티와 연관관계를 가지는 경우 트랜잭션 밖에서 접근 시 `LazyInitializationException` 발생할 수 있음에 주의
  - 하위 계층인 `Persistence` 계층의 엔티티를 `Presentation` 계층의 DTO, Controller에 위치하는 것은 역할에 따라 계층을 분리하는 레이어드 아키텍쳐 관점에서 역방향으로 참조하고 있어 각 계층의 역할 경계를 흐트릴수 있고, 협업하는 다른 개발자로 하여금 혼선을 줄 여지가 있으므로 좋은 구조가 아님
- [x] 코드 정합성 및 가독성을 위한 제네릭 형식 명시
- [x] 최단 경로 응답 객체를 적절한 path package로 이동
- [x] static 변수로 할당된 공유 변수에 매번 값을 할당하면서 발생할 수 있는 쓰레드 세이프하지 않은 문제
- [ ] 예외 처리 시, 오류 메세지 누락으로 인해 디버깅이 어려울 수 있는 문제
- [ ] 자주 사용하는 Mokcing 대상 객체의 Fake 적용
 
