package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Sections sections;
    private Station 교대역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 고속터미널역;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        고속터미널역 = new Station("고속터미널역");
        삼호선 = new Line("3호선", "orange darken-1");
        sections = new Sections();
        sections.add(new Section(삼호선, 교대역, 양재역, 10));
    }

    @Test
    void 포함된_역_목록_조회() {
        //when
        //then
        assertThat(sections.getStations()).isNotEmpty().containsExactlyInAnyOrder(교대역, 양재역);
    }

    @Test
    void 상행_종점_하행_종점_사이에_구간_삭제() {
        //given
        상행_종점_하행_종점_사이에_구간_추가();

        //when
        sections.removeLineStation(삼호선, 남부터미널역);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(교대역, 양재역);
    }

    @Test
    void 하행_종점_축소() {
        //given
        상행_종점_하행_종점_사이에_구간_추가();

        //when
        sections.removeLineStation(삼호선, 양재역);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(교대역, 남부터미널역);
    }

    @Test
    void 상행_종점_축소() {
        //given
        상행_종점_하행_종점_사이에_구간_추가();

        //when
        sections.removeLineStation(삼호선, 교대역);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(남부터미널역, 양재역);
    }

    @Test
    void 상행_종점_하행_종점_사이에_구간_추가() {
        //when
        sections.addLineStation(삼호선, 교대역, 남부터미널역, 5);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(교대역, 남부터미널역, 양재역);
    }

    @Test
    void 하행_종점_연장() {
        //when
        sections.addLineStation(삼호선, 남부터미널역, 교대역, 5);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(남부터미널역, 교대역, 양재역);
    }

    @Test
    void 상행_종점_연장() {
        //when
        sections.addLineStation(삼호선, 양재역, 남부터미널역, 5);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(교대역, 양재역, 남부터미널역);
    }

    @Test
    void 구간_비었을_때_포함된_역_목록_조회() {
        //when
        sections = new Sections();
        //then
        assertThat(sections.getStations()).isEmpty();
    }

    @Test
    void 마지막_구간이면_역을_삭제할_수_없다() {
        //then
        assertThatThrownBy(() -> sections.removeLineStation(삼호선, 양재역)).isExactlyInstanceOf(CannotRemoveException.class);
    }

    @Test
    void 이미_포함된_구간은_추가할_수_없다() {
        //then
        assertThatThrownBy(() -> sections.addLineStation(삼호선, 교대역, 양재역, 20)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 구간_내_상행_하행_역_중_하나라도_포함되어_있지_않으면_추가할_수_없다() {
        //then
        assertThatThrownBy(() -> sections.addLineStation(삼호선, 남부터미널역, 고속터미널역, 1)).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }
}
