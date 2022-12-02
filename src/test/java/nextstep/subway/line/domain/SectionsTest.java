package nextstep.subway.line.domain;

import nextstep.subway.exception.DuplicatedSectionException;
import nextstep.subway.exception.InvalidSectionDistanceException;
import nextstep.subway.exception.InvalidSectionException;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

class SectionsTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;
    private Section 강남역_광교역_구간;
    private Sections sections;


    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        강남역_광교역_구간 = new Section(신분당선, 강남역, 광교역, 10);
        sections = Sections.createEmpty();
        sections.add(강남역_광교역_구간);
    }

    @DisplayName("이미 등록된 지하철 구간을 등록하면 예외가 발생한다.")
    @Test
    void addException1() {
        Assertions.assertThatThrownBy(() -> sections.add(강남역_광교역_구간))
                .isInstanceOf(DuplicatedSectionException.class)
                .hasMessageStartingWith(ExceptionMessage.DUPLICATED_SECTION);
    }

    @DisplayName("추가할 지하철 구간의 상행역, 하행역 둘다 기존 지하철 구간에 없는 역이면 예외가 발생한다.")
    @Test
    void addException2() {
        Station 죽전역 = new Station("죽전역");
        Station 수원역 = new Station("수원역");
        Section 죽전역_수원역_구간 = new Section(신분당선, 죽전역, 수원역, 5);

        Assertions.assertThatThrownBy(() -> sections.add(죽전역_수원역_구간))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SECTION);
    }

    @DisplayName("상행역-신규역 구간을 추가할 때 기존 지하철 구간(상행역-하행역) 길이보다 크거나 같으면 예외가 발생한다. ")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {10, 20, 30})
    void addException3(int input) {
        Station 양재역 = new Station("양재역");
        Section 강남역_양재역_구간 = new Section(신분당선, 강남역, 양재역, input);

        Assertions.assertThatThrownBy(() -> sections.add(강남역_양재역_구간))
                .isInstanceOf(InvalidSectionDistanceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SECTION_DISTANCE);
    }

    @DisplayName("신규역-하행역 구간을 추가할 때 기존 지하철 구간(상행역-하행역) 길이보다 크거나 같으면 예외가 발생한다. ")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {10, 20, 30})
    void addException4(int input) {
        Station 양재역 = new Station("양재역");
        Section 양재역_광교역_구간 = new Section(신분당선, 양재역, 광교역, input);

        Assertions.assertThatThrownBy(() -> sections.add(양재역_광교역_구간))
                .isInstanceOf(InvalidSectionDistanceException.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_SECTION_DISTANCE);
    }

    @DisplayName("바어있는 지하철 구간이 새로운 지하철 구간을 추가한다.")
    @Test
    void add() {
        Sections sections = Sections.createEmpty();
        sections.add(강남역_광교역_구간);

        Assertions.assertThat(sections.getSections())
                .containsExactlyInAnyOrder(new Section(신분당선, 강남역, 광교역, 10));
    }

    @DisplayName("기존 지하철 구간에 새로운 지하철 구간을 추가한다.")
    @Test
    void add2() {
        Station 양재역 = new Station("양재역");
        sections.add(new Section(신분당선, 강남역, 양재역, 5));

        Assertions.assertThat(sections.getStations())
                .containsExactlyInAnyOrder(강남역, 양재역, 광교역);
    }

    @DisplayName("새로운 상행역으로 지하철 구간을 추가한다.")
    @Test
    void add3() {
        Station 신사역 = new Station("신사역");
        sections.add(new Section(신분당선, 신사역, 강남역, 10));

        Assertions.assertThat(sections.getStations())
                .containsExactlyInAnyOrder(신사역, 강남역, 광교역);
    }

    @DisplayName("새로운 하행역으로 지하철 구간을 추가한다.")
    @Test
    void add4() {
        Station 수원역 = new Station("수원역");

        sections.add(new Section(신분당선, 광교역, 수원역, 10));

        Assertions.assertThat(sections.getStations())
                .containsExactlyInAnyOrder(강남역, 광교역, 수원역);
    }

    @DisplayName("주어진 지하철 역 목록을 상행역과 하행역으로 가지는 지하철 구간으로부터 지하철 노선을 찾는다.")
    @Test
    void findLines() {
        Lines lines = sections.findLinesFrom(Arrays.asList(강남역, 광교역));

        Assertions.assertThat(lines.getLines().get(0)).isEqualTo(신분당선);
    }
}
