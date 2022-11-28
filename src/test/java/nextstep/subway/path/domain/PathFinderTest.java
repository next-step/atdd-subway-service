package nextstep.subway.path.domain;

import nextstep.subway.constant.ErrorCode;
import nextstep.subway.fixture.LineTestFactory;
import nextstep.subway.fixture.SectionTestFactory;
import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {
    @DisplayName("출발역과 도착역의 최단 경로를 구할 수 있다")
    @Test
    void findShortestPath() {
        // given
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");
        Station 인천역 = new Station("인천역");
        Station 부평역 = new Station("부평역");

        Line 신분당선 = LineTestFactory.create("신분당선", "bg-red-600", 강남역, 판교역, 10, 0);
        Line 일호선 = LineTestFactory.create("일호선", "bg-blue-600", 인천역, 부평역, 10, 0);
        일호선.addSection(SectionTestFactory.create(판교역, 인천역, 20));

        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 일호선));

        // when
        PathResponse response = pathFinder.shortestPath(강남역, 부평역);

        List<String> stationNames = response.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(40),
                () -> assertThat(stationNames).containsExactlyElementsOf(
                        Arrays.asList("강남역", "판교역", "인천역", "부평역")
                )
        );
    }

    @DisplayName("출발역과 도착역이 동일하면 예외가 발생한다")
    @Test
    void sameStationException() {
        // given
        Station 강남역 = new Station("강남역");
        PathFinder pathFinder = new PathFinder(
                Arrays.asList(new Line("신분당선", "bg-red-600", new ExtraCharge(0)))
        );

        // when & then
        assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FIND_PATH_SAME_SOURCE_TARGET.getMessage());
    }

    @DisplayName("최단경로를 조회하는 역이 존재하지 않으면 예외가 발생한다")
    @Test
    void notExistStationException() {
        // given
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");
        PathFinder pathFinder = new PathFinder(
                Arrays.asList(new Line("신분당선", "bg-red-600", new ExtraCharge(0)))
        );

        // when & then
        assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 판교역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FIND_PATH_NOT_EXIST.getMessage());
    }

    @DisplayName("출발역과 도착역이 연결 되어 있지 않으면 예외가 발생한다")
    @Test
    void notConnectException() {
        // given
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");
        Station 인천역 = new Station("인천역");
        Station 부평역 = new Station("부평역");

        Line 신분당선 = LineTestFactory.create("신분당선", "bg-red-600", 강남역, 판교역, 10, 0);
        Line 일호선 = LineTestFactory.create("일호선", "bg-blue-600", 인천역, 부평역, 10, 0);
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 일호선));

        // when & then
        assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 부평역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FIND_PATH_NOT_CONNECT.getMessage());
    }
}
