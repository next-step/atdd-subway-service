package nextstep.subway.path.component;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponses;

class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Section 강남_교대_구간;
    private Section 강남_양재_구간;
    private Section 교대_남부터미널_구간;
    private Section 남부터미널_양재_구간;
    private List<Section> 모든_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        이호선 = new Line("이호선", "green");
        삼호선 = new Line("삼호선", "orange");

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        강남_교대_구간 = new Section(이호선, 강남역, 교대역, 7);
        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 5);
        교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 3);
        남부터미널_양재_구간 = new Section(신분당선, 남부터미널역, 양재역, 4);

        모든_구간 = Arrays.asList(강남_교대_구간, 강남_양재_구간, 교대_남부터미널_구간, 남부터미널_양재_구간);
    }

    @DisplayName("최단 경로 찾기")
    @Test
    void findPath() {
        Sections sections = Sections.from(모든_구간);
        StationResponses stationResponses = StationResponses.from(Arrays.asList(강남역, 양재역, 남부터미널역));
        PathResponse expected = new PathResponse(stationResponses.getResponses(), 9);
        PathFinder pathFinder = new PathFinder();
        Graph<Station, DefaultWeightedEdge> graph = sections.makeGraph();

        PathResponse actual = pathFinder.findPath(graph, 강남역, 남부터미널역);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러")
    @Test
    void findPath_errorWhenNotConnected() {
        Station 정자역 = new Station("정자역");
        Station 미금역 = new Station("미금역");
        Line 분당선 = new Line("분당선", "yellow");
        Section 정자_미금_구간 = new Section(분당선, 정자역, 미금역, 10);
        Sections sections = Sections.from(모든_구간);
        sections.addSection(정자_미금_구간);
        Graph<Station, DefaultWeightedEdge> graph = sections.makeGraph();
        PathFinder pathFinder = new PathFinder();

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> pathFinder.findPath(graph, 교대역, 정자역))
            .withMessage("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }
}