package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {
    private static Station SOURCE = new Station("강남역");
    private static Station TARGET = new Station("양재역");

    private Path path;

    @BeforeEach
   void setUp() {
        path = new Path(SOURCE, TARGET);
    }

    @ParameterizedTest
    @MethodSource(value = "출발역과_도착역을_가져온다")
    void 같은_경로인지_확인한다(Station source, Station target, boolean expected) {
        // when
        boolean result = path.isSame(source, target);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> 출발역과_도착역을_가져온다() {
        return Stream.of(
                Arguments.of(
                        SOURCE, TARGET, true
                ),
                Arguments.of(
                        TARGET, SOURCE, true
                ),
                Arguments.of(
                        SOURCE, SOURCE, false
                )
        );
    }
}
