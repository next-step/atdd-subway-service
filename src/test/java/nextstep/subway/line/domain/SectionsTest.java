package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SectionsTest {

    private final static Line 신분당선 = new Line("신분당선", "red");
    private final static Station 신논현역 = new Station("신논현");
    private final static Station 강남역 = new Station("강남역");
    private final static Station 양재역 = new Station("양재역");

    @ParameterizedTest
    @MethodSource("정렬된_역_반환_파라미터")
    void 정렬된_역_반환(Station A, Station B, Station C) {
        // given
        Sections sections = new Sections();
        Section section1 = new Section(신분당선, A, B, 5);
        Section section2 = new Section(신분당선, B, C, 5);
        sections.add(section1);
        sections.add(section2);

        // when
        List<Station> actual = sections.getStationInOrder();

        // then
        assertThat(actual).containsExactly(A, B, C);
    }

    private static Stream<Arguments> 정렬된_역_반환_파라미터() {
        return Stream.of(
                arguments(신논현역, 강남역, 양재역),
                arguments(강남역, 신논현역, 양재역),
                arguments(양재역, 강남역, 신논현역)
        );
    }
}