package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.DuplicatePathException;
import nextstep.subway.path.exception.NotConnectedPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exeption.NotFoundStationException;

@DisplayName("경로 도메인 관련 기능")
public class PathTest {
    private Station 강남역, 양재역, 교대역, 남부터미널역, 선릉역, 수원역;
    private Line 분당선, 신분당선, 이호선, 삼호선;
    private Path path;

    /**               (10)
     *  교대역    --- *2호선* ---   강남역
     *  |                        |
     *  *3호선* (3)               *신분당선* (10)
     *  |               (2)      |
     *  남부터미널역  -- *3호선* -- 양재
     *
     *                (50)
     *  수원역    --- *분당선* --- 선릉역
     */
    @BeforeEach
    void setup() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        선릉역 = new Station("선릉역");
        수원역 = new Station("수원역");

        분당선 = new Line("신분당선", "bg-yellow-600", 선릉역, 수원역, 50);
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("삼호선", "bg-green-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);
        삼호선.addStation(교대역, 남부터미널역, 3);

        path = new Path(Arrays.asList(분당선, 신분당선, 이호선, 삼호선));
    }

    @Test
    @DisplayName("경로 찾기 기능 : 정상")
    void findPath() {
        // when
        int minDistance = path.findShortestDistance(강남역, 남부터미널역);
        List<Station> stations = path.findShortestPath(강남역, 남부터미널역);

        // then
        assertThat(minDistance).isEqualTo(12);
        assertThat(stations).containsExactly(강남역, 양재역, 남부터미널역);
    }

    @Test
    @DisplayName("경로 찾기 에러 : 출발역과 도착역이 같은 경우")
    void findPathOccurDuplicatePathException() {
        // when, then
        assertThatThrownBy(() -> path.findShortestPath(강남역, 강남역)).isInstanceOf(DuplicatePathException.class);
    }

    @Test
    @DisplayName("경로 찾기 에러 : 출발역과 도착역이 연결이 되어 있지 않은 경우")
    void findPathOccurNotConnectedPathException() {
        // then
        assertThatThrownBy(() -> path.findShortestPath(강남역, 수원역)).isInstanceOf(NotConnectedPathException.class);
        assertThatThrownBy(() -> path.findShortestPath(수원역, 강남역)).isInstanceOf(NotConnectedPathException.class);
    }

    @Test
    @DisplayName("경로 찾기 에러 : 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    void findPathOccurNotFoundStationException() {
        // given
        Station 잠실역 = new Station("잠실역");

        // when, then
        assertThatThrownBy(() -> path.findShortestPath(강남역, 잠실역)).isInstanceOf(NotFoundStationException.class);
        assertThatThrownBy(() -> path.findShortestPath(잠실역, 수원역)).isInstanceOf(NotFoundStationException.class);
    }
}
