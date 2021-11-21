package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("구간")
class SectionTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Section.of(
                station("강남"),
                station("역삼"),
                Distance.from(Integer.MAX_VALUE)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @MethodSource
    @DisplayName("'null' 인자가 존재한 상태로 객체화")
    void instance_emptyArgument_thrownIllegalArgumentException(
        Station upStation, Station downStation, Distance distance) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Section.of(upStation, downStation, distance))
            .withMessageEndingWith(" must not be null");
    }

    @Test
    @DisplayName("같은 역으로 객체화")
    void instance_sameUpAndDownStation_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                Section.of(station("강남"),
                    station("강남"),
                    Distance.from(Integer.MAX_VALUE)))
            .withMessageEndingWith(" must not equal");
    }

    private static Stream<Arguments> instance_emptyArgument_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(null, station("역삼"), Distance.from(10)),
            Arguments.of(station("강남"), null, Distance.from(10)),
            Arguments.of(station("강남"), station("역삼"), null)
        );
    }

    private static Station station(String name) {
        return Station.from(Name.from(name));
    }
}
