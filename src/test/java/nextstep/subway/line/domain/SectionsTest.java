package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SectionsTest {

    private final static Line 신분당선 = new Line("신분당선", "red");
    private final static Station 신논현역 = new Station("신논현");
    private final static Station 강남역 = new Station("강남역");
    private final static Station 양재역 = new Station("양재역");
    private final static Station 오금역 = new Station("오금역");
    private final static Station 송파역 = new Station("송파역");

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

    @Test
    void 역_사이에_새로운_역을_등록할_경우() {
        // given
        Sections sections = new Sections();
        Section section1 = new Section(신분당선, 신논현역, 양재역, 5);
        sections.add(section1);

        // when
        Section section2 = new Section(신분당선, 신논현역, 강남역, 3);
        int expected = section1.getDistance() - section2.getDistance();
        sections.add(section2);

        // then
        assertAll(
                () -> assertThat(section1.getDistance()).isEqualTo(expected),
                () -> assertThat(sections.getStationInOrder()).containsExactly(신논현역, 강남역, 양재역)
        );
    }

    @Test
    void 새로운_역을_상행_또는_하행_종점으로_등록할_경우() {
        // given
        Sections sections = new Sections();
        Section section1 = new Section(신분당선, 신논현역, 강남역, 5);
        sections.add(section1);

        // when
        Section section2 = new Section(신분당선, 강남역, 양재역, 3);
        sections.add(section2);

        // then
        assertThat(sections.getStationInOrder()).containsExactly(신논현역, 강남역, 양재역);
    }


    @DisplayName("역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없음")
    @Test
    void 구간_등록_시_예외_케이스_1() {
        // given
        Sections sections = new Sections();
        Section section1 = new Section(신분당선, 신논현역, 양재역, 5);
        sections.add(section1);

        // when & then
        Section section2 = new Section(신분당선, 신논현역, 강남역, 5);
        assertThatThrownBy(() -> sections.add(section2)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음")
    @Test
    void 구간_등록_시_예외_케이스_2() {
        // given
        Sections sections = new Sections();
        Section section1 = new Section(신분당선, 신논현역, 강남역, 5);
        Section section2 = new Section(신분당선, 강남역, 양재역, 3);
        sections.add(section1);
        sections.add(section2);

        // when & then
        Section section3 = new Section(신분당선, 신논현역, 양재역, 2);
        assertThatThrownBy(() -> sections.add(section3)).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없음")
    @Test
    void 구간_등록_시_예외_케이스_3() {
        // given
        Sections sections = new Sections();
        Section section1 = new Section(신분당선, 신논현역, 강남역, 5);
        Section section2 = new Section(신분당선, 강남역, 양재역, 3);
        sections.add(section1);
        sections.add(section2);

        // when & then
        Section section3 = new Section(신분당선, 오금역, 송파역, 2);
        assertThatThrownBy(() -> sections.add(section3)).isInstanceOf(IllegalArgumentException.class);
    }

}