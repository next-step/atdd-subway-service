# 인수 테스트 기반 TDD
## STEP 1. 인수 테스트 기반 리팩터링
### 요구사항
- [x] LineService 리팩터링
- [x] LineSectionAcceptanceTest 리팩터링

### 요구사항 설명
#### 인수 테스트 기반 리팩터링
- LineService의 비즈니스 로직을 도메인으로 옮기기
- 한번에 많은 부분을 고치려 하지 말고 나눠서 부분부분 리팩터링하기
- 전체 기능은 인수 테스트로 보호한 뒤 세부 기능을 TDD로 리팩터링하기


1. Domain으로 옮길 로직을 찾기
- 스프링 빈을 사용하는 객체와 의존하는 로직을 제외하고는 도메인으로 옮길 예정
- 객체지향 생활체조를 참고
2. Domain의 단위 테스트를 작성하기
- 서비스 레이어에서 옮겨 올 로직의 기능을 테스트
- SectionsTest나 LineTest 클래스가 생성될 수 있음
3. 로직을 옮기기
- 기존 로직을 지우지 말고 새로운 로직을 만들어 수행
- 정상 동작 확인 후 기존 로직 제거

#### 인수 테스트 통합
- API를 검증하기 보다는 시나리오, 흐름을 검증하는 테스트로 리팩터링 하기
- 반드시 하나의 시나리오로 통합할 필요는 없음,
- 기능의 인수 조건을 설명할 때 하나 이상의 시나리오가 필요한 경우
- 여러개의 시나리오를 만들어 인수 테스트를 작성할 수 있음

#### 인수 조건 예시
Feature: 지하철 구간 관련 기능
Background

Given 지하철역 등록되어 있음
And 지하철 노선 등록되어 있음
And 지하철 노선에 지하철역 등록되어 있음

Scenario: 지하철 구간을 관리

- [x] Scenario: 지하철 구간 등록
  When 지하철 구간 등록 요청
  Then 지하철 구간 등록됨

- [x] Scenario: 지하철 노선에 구간 등록이 반영된 역 목록 조회
  When 지하철 노선에 등록된 역 목록 조회 요청
  Then 등록한 지하철 구간이 반영된 역 목록이 조회됨

- [x] Scenario: 지하철 구간 삭제
  When 지하철 구간 삭제 요청
  Then 지하철 구간 삭제됨

- [x] Scenario: 지하철 노선에 구간 삭제가 반영된 역 목록 조회
  When 지하철 노선에 등록된 역 목록 조회 요청
  Then 삭제한 지하철 구간이 반영된 역 목록이 조회됨



## STEP 2. 경로 조회 기
### 요구사항
- [x] 최단 경로 조회 인수 테스트 만들기
- [x] 최단 경로 조회 기능 구현하기
### 요청 / 응답 포맷
Request
```http
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8
```
Response
```http
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```
```json
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
- 최단 경로 라이브러리
- jgrapht 라이브러리를 활용하면 간편하게 최단거리를 조회할 수 있음
- 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
- 정점: 지하철역(Station)
- 간선: 지하철역 연결정보(Section)
- 가중치: 거리
- 최단 거리 기준 조회 시 가중치를 거리로 설정

```java
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
- jgrapht graph-algorithms

### 외부 라이브러리 테스트
- 외부 라이브러리의 구현을 수정할 수 없기 때문에 단위 테스트를 하지 않음
- 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야 함
- 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용

### 인수 테스트 픽스쳐 예시
```java
@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }
    ...
```

### 예외 상황 예시
- [x] 출발역과 도착역이 같은 경우
- [x] 출발역과 도착역이 연결이 되어 있지 않은 경우
- [x] 존재하지 않은 출발역이나 도착역을 조회 할 경우

### 미션 수행 순서
#### 인수 테스트 성공 시키기
- mock 서버와 dto를 정의하여 인수 테스트 성공 시키기

#### 기능 구현
- TDD의 방향보다 테스트를 통해 구현할 기능을 명세하는것과 리팩터링이 더 중요합니다!

#### Outside In 경우
- 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
- 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
  외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
  Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)

#### Inside Out 경우
- 도메인 설계 후 도메인 테스트를 시작으로 기능 구현 시작
- 해당 도메인의 단위 테스트를 통해 도메인의 역할과 경계를 설계
- 도메인의 구현이 끝나면 해당 도메인과 관계를 맺는 객체에 대해 기능 구현 시작

ex) 경로 조회를 수행하는 도메인 구현 예시
1. PathFinder 라는 클래스 작성 후 경로 조회를 위한 테스트를 작성
2. 경로 조회 메서드에서 Line을 인자로 받고 그 결과로 원하는 응답을 리턴하도록 테스트 완성
3. 테스트를 성공시키기 위해 JGraph의 실제 객체를 활용(테스트에서는 알 필요가 없음)
4. 두 방향성을 모두 사용해보시고 테스트가 협력 객체의 세부 구현에 의존하는 경우(가짜 협력 객체 사용)와
5. 테스트 대상이 협력 객체와 독립적이지 못하고 변경에 영향을 받는 경우(실제 협력 객체 사용)를 모두 경험해보세요 :)

### 프론트엔드
- 구현한 기능이 정상적으로 동작하는지 확인을 하기 위한 코드입니다.
- 반드시 페이지에 맞게 설계를 할 필요는 없고 프론트엔드 코드를 수정해도 무방합니다.