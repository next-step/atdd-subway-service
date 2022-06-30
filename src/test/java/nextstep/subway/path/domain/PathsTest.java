package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PathsTest {
    private static Station 강남역 = new Station("강남역");
    private static Station 양재역 = new Station("양재역");
    private static Station 정자역 = new Station("정자역");
    private static Station 광교역 = new Station("광교역");

    private Paths paths;


    @BeforeEach
    void setUp() {
        paths = new Paths(Arrays.asList(강남역, 양재역, 정자역, 광교역));
    }

    @ParameterizedTest
    @MethodSource(value = "구간을_가져온다")
    void 경로들에_구간이_포함되어_있는지_확인한다(Section section, boolean expected) {
        // when
        boolean result = paths.contains(section);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> 구간을_가져온다() {
        return Stream.of(
                Arguments.of(
                        new Section(강남역, 양재역, 10), true
                ),
                Arguments.of(
                        new Section(양재역, 정자역, 10), true
                ),
                Arguments.of(
                        new Section(정자역, 광교역, 10), true
                ),
                Arguments.of(
                        new Section(광교역, 강남역, 10), false
                )
        );
    }
}
