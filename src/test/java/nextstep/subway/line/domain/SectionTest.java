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

    @Test
    public void 구간에_포함된_지하철역을_가진_구간인지_확인_true() {
        Section expected = new Section(신분당선, 강남역, 광교역, 4);
        assertThat(구간.isContainStation(expected)).isTrue();
    }

    @Test
    public void 구간에_포함된_지하철역을_가진_구간인지_확인_false() {
        Section expected = new Section(신분당선, new Station(7L, "교대역"), 광교역, 4);
        assertThat(구간.isContainStation(expected)).isFalse();
    }

    @Test
    void 두개의_구간의_상행_지하철역이_일치하는지_확인_true() {
        Section expected = new Section(신분당선, 강남역, 광교역, 4);
        assertThat(구간.isSameUpStation(expected)).isTrue();
    }

    @Test
    void 두개의_구간의_상행_지하철역이_일치하는지_확인_false() {
        Section expected = new Section(신분당선, new Station(7L, "교대역"), 광교역, 4);
        assertThat(구간.isSameUpStation(expected)).isFalse();
    }

    @Test
    void 두개의_구간의_하행_지하철역이_일치하는지_확인_true() {
        Section expected = new Section(신분당선, 광교역, 양재역, 4);
        assertThat(구간.isSameDownStation(expected)).isTrue();
    }

    @Test
    void 두개의_구간의_하행_지하철역이_일치하는지_확인_false() {
        Section expected = new Section(신분당선, 양재역, 광교역, 4);
        assertThat(구간.isSameDownStation(expected)).isFalse();
    }

    @Test
    void 두개의_구간의_상하행_지하철역이_모두_같은지_확인_true() {
        assertThat(구간.isSameStations(구간)).isTrue();
    }

    @Test
    void 두개의_구간의_상하행_지하철역이_모두_같은지_확인_false() {
        Section expected = new Section(신분당선, 광교역, 양재역, 4);
        assertThat(구간.isSameStations(expected)).isFalse();
    }
}
