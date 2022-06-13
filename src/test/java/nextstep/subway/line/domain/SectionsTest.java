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
        고속터미널역 = new Station("고속터미널");
        삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 10);

        sections = new Sections();
    }

    @Test
    void getStations_is_not_empty() {
        //when
        sections.add(new Section(삼호선, 고속터미널역, 교대역, 3));
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 3));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 3));

        //then
        assertThat(sections.getStations()).isNotEmpty().containsExactlyInAnyOrder(교대역, 양재역, 남부터미널역, 고속터미널역);
    }

    @Test
    void remove_is_not_empty() {
        //given
        sections.add(new Section(삼호선, 고속터미널역, 교대역, 3));
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 3));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 3));

        //when
        sections.removeLineStation(삼호선, 양재역);

        //then
        assertThat(sections.getStations()).containsExactlyInAnyOrder(교대역, 남부터미널역, 고속터미널역);
    }

    @Test
    void getStations_is_empty() {
        //then
        assertThat(sections.getStations()).isEmpty();
    }

    @Test
    void remove_is_empty() {
        //then
        assertThatThrownBy(() -> sections.removeLineStation(삼호선, 양재역)).isExactlyInstanceOf(RuntimeException.class);
    }

}
