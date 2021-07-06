package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Path;
import nextstep.subway.path.exception.CannotReachableException;
import nextstep.subway.path.exception.IllegalFindingPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("PathFinder 테스트")
class PathFinderTest {

    private PathFinder pathFinder = new PathFinder();

    private static Station 대구역 = new Station(0L, "대구역");
    private static Station 양평역 = new Station(1L, "양평역");
    private static Station 영등포구청역 = new Station(2L, "영등포구청");
    private static Station 영등포시장역 = new Station(3L, "영등포시장");
    private static Station 신길역 = new Station(4L, "신길역");
    private static Station 여의도역 = new Station(5L, "여의도역");
    private static Station 당산역 = new Station(6L, "당산역");
    private static Station 영등포역 = new Station(7L, "영등포역");
    private static Station 야탑역 = new Station(8L, "야탑역");
    private static Station 모란역 = new Station(9L, "모란역");

    private static Line 오호선 = new Line(1L, "오호선", "bg-red-600", 양평역, 영등포구청역, new Distance(10));
    private static Line 이호선 = new Line(2L, "이호선", "bg-red-600", 영등포구청역, 당산역, new Distance(10));
    private static Line 일호선 = new Line(3L, "일호선", "bg-red-600", 신길역, 영등포역, new Distance(5));
    private static Line 신분당선 = new Line(4L, "신분당선", "bg-red-600", 야탑역, 모란역, new Distance(5));


    private static Section 구간_양평역_영등포구청역 = new Section(1L, 오호선, 양평역, 영등포구청역, new Distance(10));
    private static Section 구간_영등포구청역_영등포시장역 = new Section(2L, 오호선, 영등포구청역, 영등포시장역, new Distance(5));
    private static Section 구간_영등포시장역_신길역 = new Section(3L, 오호선, 영등포시장역, 신길역, new Distance(10));
    private static Section 구간_신길역_여의도역 = new Section(4L, 오호선, 신길역, 여의도역, new Distance(10));
    private static Section 구간_영등포구청역_당산역 = new Section(5L, 이호선, 영등포구청역, 당산역, new Distance(10));
    private static Section 구간_신길역_영등포역 = new Section(6L, 오호선, 신길역, 영등포역, new Distance(5));
    private static Section 구간_야탑역_모란역 = new Section(7L, 신분당선, 야탑역, 모란역, new Distance(5));

    private static Sections 전체_구간 = new Sections(asList(구간_양평역_영등포구청역
            , 구간_영등포구청역_영등포시장역
            , 구간_영등포시장역_신길역
            , 구간_신길역_여의도역
            , 구간_영등포구청역_당산역
            , 구간_영등포구청역_영등포시장역
            , 구간_신길역_영등포역
            , 구간_야탑역_모란역));

    @MethodSource("methodSource_findShortestPath_성공")
    @ParameterizedTest
    void findShortestPath_성공(Station sourceStation, Station targetStation, Stations stations, Distance distance) {
        Path path = pathFinder.findShortestPath(전체_구간, sourceStation, targetStation);

        assertThat(path.getStations()).isEqualTo(stations);
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    static Stream<Arguments> methodSource_findShortestPath_성공() {
        return Stream.of(
                Arguments.of(영등포구청역, 신길역, new Stations(asList(영등포구청역, 영등포시장역, 신길역)), new Distance(15)),
                Arguments.of(당산역, 양평역, new Stations(asList(당산역, 영등포구청역, 양평역)), new Distance(20)),
                Arguments.of(영등포역, 당산역, new Stations(asList(영등포역, 신길역, 영등포시장역, 영등포구청역, 당산역)), new Distance(30))
        );
    }


    @MethodSource("methodSource_findShortestPath_예외")
    @ParameterizedTest
    void findShortestPath_예외(Station source, Station target, Class<? extends RuntimeException> expectedException) {
        assertThatExceptionOfType(expectedException)
                .isThrownBy(() -> pathFinder.findShortestPath(전체_구간, source, target));
    }

    static Stream<Arguments> methodSource_findShortestPath_예외() {
        return Stream.of(
                Arguments.of(영등포구청역, 영등포구청역, IllegalFindingPathException.class),
                Arguments.of(대구역, 양평역, IllegalFindingPathException.class),
                Arguments.of(양평역, 대구역, IllegalFindingPathException.class),
                Arguments.of(영등포구청역, 모란역, CannotReachableException.class)
        );
    }
}