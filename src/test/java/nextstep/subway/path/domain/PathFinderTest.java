package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("최단경로 계산 단위테스트")
class PathFinderTest {
    PathFinder pathFinder;
    Station 강남역;
    Station 남부터미널역;
    Station 교대역;
    Station 양재역;
    List<Line> lines;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        pathFinder = new PathFinder();

        강남역 = new Station("강남역");
        남부터미널역 = new Station("남부터미널역");
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addStation(교대역, 남부터미널역, 3);
        lines = Arrays.asList(신분당선, 이호선, 삼호선);
    }

    @DisplayName("최단경로와 최단거리를 계산한다")
    @Test
    void findPath() {
        PathResponse response = pathFinder.findPath(lines, 강남역, 남부터미널역);
        List<String> stationNames = response.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(stationNames).containsExactly("강남역", "양재역", "남부터미널역");
            softAssertions.assertThat(response.getDistance()).isEqualTo(12);
        });
    }
}
