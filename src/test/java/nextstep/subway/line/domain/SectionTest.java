package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {
    private Line 신분당선;

    private Station 강남역;
    private Station 판교역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");
    }

    @Test
    void updateUpStation() {
        Section section = new Section(신분당선, 강남역, 판교역, 10);

        section.updateUpStation(양재역, 2);

        assertThat(section).isEqualTo(new Section(신분당선, 양재역, 판교역, 8));
    }

    @Test
    void updateDownStation() {
        Section section = new Section(신분당선, 강남역, 판교역, 10);

        section.updateDownStation(양재역, 2);

        assertThat(section).isEqualTo(new Section(신분당선, 강남역, 양재역, 8));
    }

}