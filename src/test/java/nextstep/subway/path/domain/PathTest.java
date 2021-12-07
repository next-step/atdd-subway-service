package nextstep.subway.path.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 테스트")
class PathTest {

    @Test
    @DisplayName("시작역과 도착역이 같을때 예외처리")
    void sameStartAndEndStationTest(){
        Station dangsanStation = new Station("당산역");
        Station hapJeongStation = new Station("합정역");
        Station ehwaStation = new Station("이대역");

        Line line = new Line("2호선", "green", dangsanStation, ehwaStation, 10);

        Section section = new Section(line, dangsanStation, hapJeongStation, 3);
        line.addSection(section);

        Assertions.assertThatThrownBy(() -> {
                    new Path(dangsanStation, dangsanStation);
                }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.THERE_IS_SAME_STATIONS.errorMessage());
    }

    @Test
    @DisplayName("path stations 불러오는 테스트")
    void searchAllStationsTest(){
        Station dangsanStation = new Station("당산역");
        Station hapJeongStation = new Station("합정역");
        Station ehwaStation = new Station("이대역");

        Line twoLine = new Line("2호선", "green", dangsanStation, ehwaStation, 10);


        Section section = new Section(twoLine, dangsanStation, hapJeongStation, 3);
        twoLine.addSection(section);

        Station sunyudoStation = new Station("선유도역");
        Station shinMokDongStation = new Station("신목동역");

        Line nineLine = new Line("9호선", "gold", dangsanStation, sunyudoStation, 20);

        Section nineSection = new Section(nineLine, sunyudoStation, shinMokDongStation, 3);
        nineLine.addSection(nineSection);

        Path path = new Path(dangsanStation, sunyudoStation);
        List<Station> stations = path.stations(Arrays.asList(twoLine, nineLine));

        assertThat(stations.size()).isEqualTo(5);
        assertThat(stations).contains(dangsanStation, hapJeongStation, ehwaStation, sunyudoStation, shinMokDongStation);

    }

    @Test
    @DisplayName("최단거리 테스트")
    void searchShortestPathTest(){
        Station dangsanStation = new Station("당산역");
        Station hapJeongStation = new Station("합정역");
        Station ehwaStation = new Station("이대역");

        Line twoLine = new Line("2호선", "green", dangsanStation, ehwaStation, 10);

        Section section = new Section(twoLine, dangsanStation, hapJeongStation, 3);
        twoLine.addSection(section);

        Station sunyudoStation = new Station("선유도역");
        Station shinMokDongStation = new Station("신목동역");

        Line nineLine = new Line("9호선", "gold", dangsanStation, sunyudoStation, 20);

        Section nineSection = new Section(nineLine, sunyudoStation, shinMokDongStation, 3);
        nineLine.addSection(nineSection);

        Path path = new Path(dangsanStation, sunyudoStation);
        List<Line> lines = Arrays.asList(twoLine, nineLine);
        List<Station> stationPaths = path.findShortesetPath(lines);
        checkValidateStation(stationPaths, "당산역", "선유도역");

    }

    @Test
    @DisplayName("최단거리 source와 target이 Line에 속하지 않은 역이 있을 경우 테스트")
    void searchStationNotBelongtoLineTest(){
        Station dangsanStation = new Station("당산역");
        Station hapJeongStation = new Station("합정역");
        Station ehwaStation = new Station("이대역");
        Station seoulStation = new Station("서울역");

        Line twoLine = new Line("2호선", "green", dangsanStation, ehwaStation, 10);

        Section section = new Section(twoLine, dangsanStation, hapJeongStation, 3);
        twoLine.addSection(section);

        Station sunyudoStation = new Station("선유도역");
        Station shinMokDongStation = new Station("신목동역");

        Line nineLine = new Line("9호선", "gold", dangsanStation, sunyudoStation, 20);

        Section nineSection = new Section(nineLine, sunyudoStation, shinMokDongStation, 3);
        nineLine.addSection(nineSection);

        Path path = new Path(dangsanStation, seoulStation);
        List<Line> lines = Arrays.asList(twoLine, nineLine);
        Assertions.assertThatThrownBy(() -> {
                    path.findShortesetPath(lines);
                }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.IT_CAN_NOT_SEARCH_SOURCE_AND_TARGET_ON_LINE.errorMessage());
    }



    @Test
    @DisplayName("최단거리 외부라이브러리 테스트")
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

    private void checkValidateStation(List<Station> stations, String... stationNames) {
        List<String> stationNameBasket = stations.stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());
        assertThat(stationNameBasket).contains(stationNames);
    }

}
