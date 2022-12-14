package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static nextstep.subway.utils.Message.*;

class SectionsTest {
    private Line 신분당선;
    private Station 강남역;
    private Station 정자역;
    private Section 강남역_정자역_구간;
    private Sections sections;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        정자역 = new Station("정자역");
        강남역_정자역_구간 = new Section(신분당선, 강남역, 정자역, 10);
        sections = Sections.initSections();
        sections.add(강남역_정자역_구간);
    }

    @DisplayName("중복된 구간을 등록하면 예외가 발생한다.")
    @Test
    void sameSectionException() {
        Assertions.assertThatThrownBy(() -> sections.add(강남역_정자역_구간))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(DUPLICATED_SECTION);
    }

    @DisplayName("노선에 포함되지 않는 역이 있는 구간을 포함하면 에러가 발생한다")
    @Test
    void notContainedSectionException() {
        Station 서현역 = new Station("서현역");
        Station 왕십리역 = new Station("왕십리역");
        Section 서현역_왕십리역_구간 = new Section(신분당선, 서현역, 왕십리역, 15);

        Assertions.assertThatThrownBy(() -> sections.add(서현역_왕십리역_구간))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(INVALID_SECTION);
    }

    @DisplayName("상행역을 등록할 때 기존 구간 길이보다 크거나 같으면 예외가 발생한다. ")
    @ParameterizedTest
    @ValueSource(ints = {10, 20})
    void upStationsSizeException(int input) {
        Station 역삼역 = new Station("역삼역");
        Section 강남역_역삼역_구간 = new Section(신분당선, 강남역, 역삼역, input);

        Assertions.assertThatThrownBy(() -> sections.add(강남역_역삼역_구간))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(INVALID_OVER_SECTION_DISTANCE);
    }

    @DisplayName("하행역을 등록할 때 기존 구간 길이보다 크거나 같으면 예외가 발생한다. ")
    @ParameterizedTest
    @ValueSource(ints = {10, 20})
    void downStationSizeException(int input) {
        Station 역삼역 = new Station("역삼역");
        Section 역삼역_정자역_구간 = new Section(신분당선, 역삼역, 정자역, input);

        Assertions.assertThatThrownBy(() -> sections.add(역삼역_정자역_구간))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(INVALID_OVER_SECTION_DISTANCE);
    }

    @DisplayName("기존 지하철 구간에 새로운 종점이 아닌 지하철 구간을 추가한다.")
    @Test
    void addNotLastSection() {
        Station 역삼역 = new Station("역삼역");
        sections.add(new Section(신분당선, 강남역, 역삼역, 3));

        Assertions.assertThat(sections.getStations())
                .containsExactlyInAnyOrder(강남역, 역삼역, 정자역);
    }

    @DisplayName("기존 지하철 구간에 상행 종점 지하철 구간을 추가한다.")
    @Test
    void addLastUpStationSection() {
        Station 역삼역 = new Station("역삼역");
        sections.add(new Section(신분당선, 역삼역, 강남역, 5));

        Assertions.assertThat(sections.getStations())
                .containsExactlyInAnyOrder(역삼역, 강남역, 정자역);
    }

    @DisplayName("기존 지하철 구간에 하행 종점 지하철 구간을 추가한다.")
    @Test
    void addLastDownStationSection() {
        Station 역삼역 = new Station("역삼역");
        sections.add(new Section(신분당선, 정자역, 역삼역, 5));

        Assertions.assertThat(sections.getStations())
                .containsExactlyInAnyOrder(강남역, 정자역, 역삼역);
    }

    @DisplayName("주어진 지하철 역 목록을 상행역과 하행역으로 가지는 지하철 구간으로부터 지하철 노선을 찾는다.")
    @Test
    void findLines() {
        Lines lines = sections.findLinesFrom(Arrays.asList(강남역, 정자역));

        Assertions.assertThat(lines.getLines().get(0)).isEqualTo(신분당선);
    }





}
