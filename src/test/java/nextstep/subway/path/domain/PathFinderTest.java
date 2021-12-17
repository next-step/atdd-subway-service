package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.path.application.FarePolicy;
import nextstep.subway.path.dto.PathDtos;
import nextstep.subway.station.domain.Station;

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

    private FarePolicy farePolicy;

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
        farePolicy = new BasicFarePolicy();
    }

    @DisplayName("최단 경로 찾기")
    @Test
    void pathFinder() {
        PathDtos paths = PathDtos.from(모든_구간);
        StationGraph graph = new StationGraph(paths);

        PathFinder pathFinder = new PathFinder(graph, 강남역, 남부터미널역);

        assertThat(pathFinder.getStations()).isEqualTo(new Stations(Arrays.asList(강남역, 양재역, 남부터미널역)));
        assertThat(pathFinder.getDistance()).isEqualTo(new Distance(9));
    }

    @DisplayName("시작점과 출발점이 동일할 때 에러")
    @Test
    void findPath_errorWhenSameSourceAndTarget() {
        PathDtos paths = PathDtos.from(모든_구간);
        StationGraph graph = new StationGraph(paths);

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> new PathFinder(graph, 교대역, 교대역))
            .withMessage("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러")
    @Test
    void findPath_errorWhenNotConnected() {
        Station 정자역 = new Station("정자역");
        Station 미금역 = new Station("미금역");
        Line 분당선 = new Line("분당선", "yellow");
        Section 정자_미금_구간 = new Section(분당선, 정자역, 미금역, 10);
        List<Section> sections = Arrays.asList(강남_교대_구간, 강남_양재_구간, 교대_남부터미널_구간, 남부터미널_양재_구간, 정자_미금_구간);

        PathDtos paths = PathDtos.from(sections);
        StationGraph graph = new StationGraph(paths);

        assertThatExceptionOfType(SubwayException.class)
            .isThrownBy(() -> new PathFinder(graph, 교대역, 정자역))
            .withMessage("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }
}