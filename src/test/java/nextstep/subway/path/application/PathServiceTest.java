package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private Station gangnam;
    private Station yangjae;
    private Station gyodae;
    private Station hongdae;
    private Station gongduck;
    private Station itaewon;
    private Line line;
    private Line line2;
    private Line line3;
    private Member teenager;
    private Member child;
    private Member member;
    private List<Line> lines = new ArrayList<>();
    private PathService pathService;

    @Mock
    private StationService stationService;
    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        gangnam = new Station(1L, "강남역");
        yangjae = new Station(2L, "양재역");
        gyodae = new Station(3L, "교대역");
        hongdae = new Station(4L, "홍대역");
        gongduck = new Station(5L, "공덕역");
        itaewon = new Station(6L, "이태원역");
        line = new Line(1L,"2호선", "green", gangnam, yangjae, 0L, 10);
        line2 = new Line(2L,"3호선", "orange", yangjae, hongdae, 600L,10);
        line3 = new Line(3L,"6호선", "orange", gongduck, itaewon, 880L,10);
        line.addSection(gangnam, gyodae, 5);
        lines.add(line);
        lines.add(line2);
        lines.add(line3);
        teenager = new Member(1L, "email@email.com", "password", 15);
        child = new Member(2L, "email@email.com", "password", 7);
        member = new Member(3L, "email@email.com", "password", 20);
        pathService = new PathService(stationService, lineRepository);
    }

    @Test
    @DisplayName("경로찾기 테스트")
    void findPath() {
        mockLine(lines);
        mockStation(gangnam);
        mockStation(yangjae);
        mockStation(gyodae);
        mockStation(hongdae);

        PathResponse pathResponse = findPath(member, gangnam, hongdae);

        checkDistance(pathResponse, 20L);
    }

    @Test
    @DisplayName("요금구하기 테스트")
    void findFare() {
        mockLine(lines);
        mockStation(gangnam);
        mockStation(yangjae);
        mockStation(gyodae);
        mockStation(hongdae);

        PathResponse pathResponse1 = findPath(teenager, gangnam, hongdae);
        PathResponse pathResponse2 = findPath(child, gangnam, hongdae);
        PathResponse pathResponse3 = findPath(member, gangnam, hongdae);

        checkFare(pathResponse1, 1934L);
        checkFare(pathResponse2, 1340L);
        checkFare(pathResponse3, 2330L);
    }

    @Test
    @DisplayName("경로찾기 예외처리: 동일한 역을 조회했을 경우")
    void findSameStationsPath() {
        assertThatThrownBy(() -> {
            pathService.findPathByIds(member.getAge(), gangnam.getId(), gangnam.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("경로찾기 예외처리: 연결되지 않은 역을 조회했을 경우")
    void findNotConnectedPath() {
        assertThatThrownBy(() -> {
            pathService.findPathByIds(member.getAge(), gangnam.getId(), gongduck.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("경로찾기 예외처리: 존재하지 않은 역을 조회했을 경우")
    void findNotExistStationPath() {
        assertThatThrownBy(() -> {
            pathService.findPathByIds(member.getAge(),20L, gongduck.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
        List<DefaultWeightedEdge> edgeList
                = dijkstraShortestPath.getPath("v3", "v1").getEdgeList();
        double weight
                = dijkstraShortestPath.getPath("v3", "v1").getWeight();

        shortestPath.forEach(vertex -> System.out.println(vertex));
        System.out.println(weight);
        assertThat(shortestPath.size()).isEqualTo(3);
    }


    private void checkDistance(PathResponse pathResponse, Long expectedDistance) {
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private void checkFare(PathResponse pathResponse, Long expectedFare) {
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getFare()).isNotNull();
        assertThat(pathResponse.getFare()).isEqualTo(expectedFare);
    }

    private PathResponse findPath(Member member, Station source, Station target) {
        return pathService.findPathByIds(member.getAge(), source.getId(), target.getId());
    }

    private void mockLine(List<Line> lines) {
        when(lineRepository.findAll()).thenReturn(lines);
    }

    private void mockStation(Station station) {
        when(stationService.findStationById(station.getId())).thenReturn(station);
    }
}
