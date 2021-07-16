package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Age;
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
import static nextstep.subway.line.fixture.SectionFixture.*;
import static nextstep.subway.station.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("PathFinder 테스트")
class PathFinderTest {

    private PathFinder pathFinder = new PathFinder();

    @MethodSource("methodSource_findShortestPath_성공")
    @ParameterizedTest
    void findShortestPath_성공(Station sourceStation, Station targetStation, Age age, Stations stations, Distance distance) {
        Path path = pathFinder.findShortestPath(전체_구간, age, sourceStation, targetStation);

        assertThat(path.getStations()).isEqualTo(stations);
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    static Stream<Arguments> methodSource_findShortestPath_성공() {
        return Stream.of(
                Arguments.of(영등포구청역, 신길역, new Age(20), new Stations(asList(영등포구청역, 영등포시장역, 신길역)), new Distance(15)),
                Arguments.of(당산역, 양평역, new Age(6), new Stations(asList(당산역, 영등포구청역, 양평역)), new Distance(20)),
                Arguments.of(영등포역, 당산역, new Age(13), new Stations(asList(영등포역, 신길역, 영등포시장역, 영등포구청역, 당산역)), new Distance(30))
        );
    }


    @MethodSource("methodSource_findShortestPath_예외")
    @ParameterizedTest
    void findShortestPath_예외(Station source, Station target, Class<? extends RuntimeException> expectedException) {
        assertThatExceptionOfType(expectedException)
                .isThrownBy(() -> pathFinder.findShortestPath(전체_구간, new Age(20), source, target));
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