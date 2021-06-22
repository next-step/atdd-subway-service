package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 양재역;
    private Station 광교역;
    private Section 구간;
    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(3L, "양재역");
        광교역 = new Station(2L, "광교역");
        구간 = new Section(신분당선, 강남역, 양재역, 10);
    }

    @Test
    void parameter로_주어진_구간_객체와_비교하여_상위_구간_반() {
        assertThat(구간.calcFirstSection(new Section(신분당선, 양재역, 광교역, 10))).isEqualTo(구간);
        assertThat(구간.calcFirstSection(new Section(신분당선, 강남역, 양재역, 10))).isEqualTo(new Section(신분당선, 강남역, 양재역, 10));
    }

    @Test
    void 다음_구간인지_확인() {
        Section section = new Section(신분당선, 양재역, 광교역, 10);
        assertThat(section.isNextSection(구간)).isTrue();
        assertThat(구간.isNextSection(section)).isFalse();
    }
}
