package nextstep.subway.path.domain;

import static nextstep.subway.line.step.SectionStep.section;
import static nextstep.subway.station.step.StationStep.station;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("경로")
class PathTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Path.of(
                Stations.from(Arrays.asList(station("강남역"), station("양재역"))),
                Distance.from(5),
                Sections.from(section("강남역", "양재역", 10))
            ));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @MethodSource
    @DisplayName("지하철 역들, 거리, 구간들은 필수")
    void instance_nullArgument_thrownIllegalArgumentException(
        Stations stations, Distance distance, Sections sections) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Path.of(stations, distance, sections))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("경로를 구하기 위한 2개 이상의 지하철 역은 필수")
    void instance_lessThanTwo_thrownIllegalArgumentException() {
        // given, when
        ThrowingCallable instanceCallable = () -> Path.of(
            Stations.from(Collections.singletonList(station("강남역"))),
            Distance.from(1),
            Sections.from(section("강남역", "양재역", 10))
        );

        // then
        assertThatIllegalArgumentException()
            .isThrownBy(instanceCallable)
            .withMessageStartingWith("경로의 지하철 역들은 적어도 ");
    }

    private static Stream<Arguments> instance_nullArgument_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(
                null,
                Distance.from(1),
                Sections.from(section("강남역", "양재역", 10))
            ),
            Arguments.of(
                Stations.from(Arrays.asList(station("강남역"), station("양재역"))),
                null,
                Sections.from(section("강남역", "양재역", 10))
            ),
            Arguments.of(
                Stations.from(Arrays.asList(station("강남역"), station("양재역"))),
                Distance.from(1),
                null
            )
        );
    }
}
