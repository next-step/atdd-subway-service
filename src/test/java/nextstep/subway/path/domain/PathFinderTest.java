package nextstep.subway.path.domain;

import nextstep.subway.exception.CannotFindPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.PathExceptionCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("거리 탐색 클래스 테스트")
class PathFinderTest {

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("2호선", "bg-green-600", 0);
        신분당선 = new Line("신분당선", "bg-red-600", 0);
        삼호선 = new Line("3호선", "bg-orange-600", 0);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        역삼역 = new Station("역삼역");
    }

    @Test
    void 출발역과_도착역이_같으면_CannotFindPathException_발생() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(new Section(이호선, 교대역, 강남역, 10)));

        assertThatThrownBy(() -> {
            pathFinder.getShortestPath(강남역, 강남역);
        }).isInstanceOf(CannotFindPathException.class)
                .hasMessage(PathExceptionCode.CANNOT_EQUALS_SOURCE_TARGET.getMessage());
    }

    @Test
    void 출발역이_StationGraph에_포함되어_있지_않으면_CannotFindPathException_발생() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(new Section(이호선, 강남역, 역삼역, 10)));

        assertThatThrownBy(() -> {
            pathFinder.getShortestPath(교대역, 강남역);
        }).isInstanceOf(CannotFindPathException.class)
                .hasMessage(PathExceptionCode.NO_LINES_CONTAINING_STATION.getMessage());
    }

    @Test
    void 도착역이_StationGraph에_포함되어_있지_않으면_CannotFindPathException_발생() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(new Section(이호선, 교대역, 강남역, 10)));

        assertThatThrownBy(() -> {
            pathFinder.getShortestPath(강남역, 역삼역);
        }).isInstanceOf(CannotFindPathException.class)
                .hasMessage(PathExceptionCode.NO_LINES_CONTAINING_STATION.getMessage());
    }

    @Test
    void 출발역과_도착역까지의_구간_중_최단_거리를_찾음() {
        Section 강남역_양재역 = new Section(신분당선, 강남역, 양재역, 10);
        Section 교대역_강남역 = new Section(이호선, 교대역, 강남역, 10);
        Section 교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 3);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 2);

        PathFinder pathFinder = new PathFinder(Arrays.asList(
                강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));

        assertThat(pathFinder.getShortestPath(양재역, 교대역)).satisfies(path -> {
            assertThat(path.getStations()).containsExactly(양재역, 남부터미널역, 교대역);
            assertEquals(5, path.getDistance());
        });
    }
}
