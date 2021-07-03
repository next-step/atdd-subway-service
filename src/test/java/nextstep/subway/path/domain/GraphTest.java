package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GraphTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Graph graph;
    private List<Line> lines;

    /**
     * 교대역 -- *2호선 거리:100* -- 강남역
     * |                        |
     * *3호선 거리:8*            *신분당선 거리:30*
     * |                        |
     * 남부터미널역-- *3호선 거리:65* --양재
     */
    @BeforeEach
    public void setUp() {
        교대역 = new Station(1L, "교대역");
        강남역 = new Station(2L, "강남역");
        양재역 = new Station(3L, "양재역");
        남부터미널역 = new Station(4L, "남부터미널역");

        Section 이호선생성구간 = new Section(1L, 교대역, 강남역, 100);
        Section 삼호선생성구간 = new Section(2L, 교대역, 양재역, 73);
        Section 삼호선추가구간 = new Section(3L, 교대역, 남부터미널역, 8);
        Section 신분당선생성구간 = new Section(4L, 강남역, 양재역, 30);

        이호선 = new Line(1L, "이호선", "초록색", 200);
        삼호선 = new Line(2L, "삼호선", "주황색", 300);
        신분당선 = new Line(3L, "신분당선", "빨간색", 900);

        이호선.addSection(이호선생성구간);
        삼호선.addSection(삼호선생성구간);
        삼호선.addSection(삼호선추가구간);
        신분당선.addSection(신분당선생성구간);

        graph = new Graph();
        lines = new ArrayList<>(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    @DisplayName("그래프 빌드")
    @Test
    public void 그래프_빌드_확인() throws Exception {
        //when
        graph.build(lines);

        //then
        assertThat(graph.getVertexes()).contains(교대역, 강남역, 양재역, 남부터미널역);
        assertThat(graph.getVertexes().size()).isEqualTo(4);
    }

    @DisplayName("최단경로 역목록 조회")
    @Test
    public void 최단경로_역목록조회_확인() throws Exception {
        //when
        Path path = graph.findShortestPath(lines, 강남역, 남부터미널역);

        //then
        assertThat(path.getStations()).containsExactly(강남역, 양재역, 남부터미널역);
    }

    @DisplayName("최단경로 거리 조회")
    @Test
    public void 최단경로_거리조회_확인() throws Exception {
        //when
        Path path = graph.findShortestPath(lines, 강남역, 남부터미널역);

        //then
        assertThat(path.getDistance()).isEqualTo(new ShortestDistance(95));
    }

    @DisplayName("최단경로 거리 조회 - 출발역과 도착역이 같은 경우")
    @Test
    public void 출발역과도착역이같은경우_최단경로거리_조회() {
        //when
        Path path = graph.findShortestPath(lines, 강남역, 강남역);

        //then
        assertThat(path.getDistance()).isEqualTo(new ShortestDistance(0));
    }

    @DisplayName("최단경로 역목록 조회 - 출발역과 도착역이 같은 경우")
    @Test
    public void 출발역과도착역이같은경우_최단경로역목록_조회() {
        //when
        Path path = graph.findShortestPath(lines, 강남역, 강남역);

        //then
        assertThat(path.getStations()).containsExactly(강남역);
    }

    @DisplayName("최단경로 역목록 조회 - 출발역과 도착역이 연결되어 있지 않는 경우")
    @Test
    public void 출발역과도착역이연결되지않은경우_최단경로역목록_조회() {
        Station 새로운상행종점역 = new Station(5L, "새로운상행종점역");
        Station 새로운하행종점역 = new Station(6L, "새로운하행종점역");
        Section 새로운노선생성구간 = new Section(5L, 새로운상행종점역, 새로운하행종점역, 10);
        Line 새로운노선 = new Line(4L, "새로운노선", "새로운색");
        새로운노선.addSection(새로운노선생성구간);
        List<Line> lines = new ArrayList<>(Arrays.asList(이호선, 삼호선, 신분당선, 새로운노선));

        //when
        //then
        assertThatThrownBy(() -> graph.findShortestPath(lines, 강남역, 새로운상행종점역))
                .hasMessage("경로가 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단경로 거리 조회 - 출발역과 도착역이 연결되어 있지 않는 경우")
    @Test
    public void 출발역과도착역이연결되지않은경우_최단경로거리_조회() {
        Station 새로운상행종점역 = new Station(5L, "새로운상행종점역");
        Station 새로운하행종점역 = new Station(6L, "새로운하행종점역");
        Section 새로운노선생성구간 = new Section(5L, 새로운상행종점역, 새로운하행종점역, 10);
        Line 새로운노선 = new Line(4L, "새로운노선", "새로운색");
        새로운노선.addSection(새로운노선생성구간);
        List<Line> lines = new ArrayList<>(Arrays.asList(이호선, 삼호선, 신분당선, 새로운노선));

        //when
        //then
        assertThatThrownBy(() -> graph.findShortestPath(lines, 강남역, 새로운상행종점역))
                .hasMessage("경로가 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단경로 역목록 조회 - 존재하지않는 도착역으로 조회할 경우")
    @Test
    public void 존재하지않는도착역으로_최단경로역목록_조회() {
        Station 존재하지않는역 = new Station(5L, "존재하지않는역");

        //when
        //then
        assertThatThrownBy(() -> graph.findShortestPath(lines, 강남역, 존재하지않는역))
                .hasMessage("graph must contain the sink vertex")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단경로 역목록 조회 - 존재하지않는 출발역으로 조회할 경우")
    @Test
    public void 존재하지않는출발역으로_최단경로역목록_조회() {
        Station 존재하지않는역 = new Station(5L, "존재하지않는역");

        //when
        //then
        assertThatThrownBy(() -> graph.findShortestPath(lines, 존재하지않는역, 강남역))
                .hasMessage("graph must contain the source vertex")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단경로 거리 조회 - 존재하지않는 도착역으로 조회할 경우")
    @Test
    public void 존재하지않는도착역으로_최단경로거리_조회() {
        Station 존재하지않는역 = new Station(5L, "존재하지않는역");

        //when
        //then
        assertThatThrownBy(() -> graph.findShortestPath(lines, 강남역, 존재하지않는역))
                .hasMessage("graph must contain the sink vertex")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단경로 거리 조회 - 존재하지않는 출발역으로 조회할 경우")
    @Test
    public void 존재하지않는출발역으로_최단경로거리_조회() {
        Station 존재하지않는역 = new Station(5L, "존재하지않는역");

        //when
        //then
        assertThatThrownBy(() -> graph.findShortestPath(lines, 존재하지않는역, 강남역))
                .hasMessage("graph must contain the source vertex")
                .isInstanceOf(IllegalArgumentException.class);
    }
}
