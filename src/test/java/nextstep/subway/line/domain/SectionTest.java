package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
    }

    @DisplayName("지하철 노선, 상행역, 하행역이 같은 두 지하철 구간은 동등하다.")
    @Test
    void equals() {
        Line line2 = new Line("신분당선", "red");
        Station 강남역2 = new Station("강남역");
        Station 광교역2 = new Station("광교역");

        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Section section2 = new Section(line2, 강남역2, 광교역2, 10);

        Assertions.assertThat(section1).isEqualTo(section2);
    }

    @DisplayName("지하철 노선이 다른 두 지하철 구간은 동등하지 않다.")
    @Test
    void notEquals1() {
        Line line2 = new Line("분당선", "yellow");
        Station 강남역2 = new Station("강남역");
        Station 광교역2 = new Station("광교역");

        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Section section2 = new Section(line2, 강남역2, 광교역2, 10);

        Assertions.assertThat(section1).isNotEqualTo(section2);
    }

    @DisplayName("상행역이 다른 두 지하철 구간은 동등하지 않다.")
    @Test
    void notEquals2() {
        Line line2 = new Line("신분당선", "red");
        Station 신사역 = new Station("신사역");
        Station 광교역2 = new Station("광교역");

        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Section section2 = new Section(line2, 신사역, 광교역2, 10);

        Assertions.assertThat(section1).isNotEqualTo(section2);
    }

    @DisplayName("하행역이 다른 두 지하철 구간은 동등하지 않다.")
    @Test
    void notEquals3() {
        Line line2 = new Line("신분당선", "red");
        Station 강남역2 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        Section section1 = new Section(신분당선, 강남역, 광교역, 10);
        Section section2 = new Section(line2, 강남역2, 양재역, 10);

        Assertions.assertThat(section1).isNotEqualTo(section2);
    }

    @DisplayName("상행역 변경 시 기존 상행역-하행역 거리보다 같거나 클경우 예외가 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | argumentsWithNames")
    @ValueSource(ints = {10, 20, 30})
    void updateUpStation(int input) {
        Section 강남역_광교역_구간 = new Section(신분당선, 강남역, 광교역, 10);

        Station 양재역 = new Station("양재역");
        Assertions.assertThatThrownBy(() -> 강남역_광교역_구간.updateUpStation(양재역, input))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("하행역 변경 시 기존 상행역-하행역 거리보다 같거나 클경우 예외가 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | argumentsWithNames")
    @ValueSource(ints = {10, 20, 30})
    void updateDownStation(int input) {
        Section 강남역_광교역_구간 = new Section(신분당선, 강남역, 광교역, 10);

        Station 양재역 = new Station("양재역");
        Assertions.assertThatThrownBy(() -> 강남역_광교역_구간.updateDownStation(양재역, input))
                .isInstanceOf(RuntimeException.class)
                .hasMessageStartingWith("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }
}
