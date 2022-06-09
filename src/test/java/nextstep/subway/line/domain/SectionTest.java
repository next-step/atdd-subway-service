package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionTest {

    private Section section;
    private Station 잠실역;
    private Station 강남역;

    @BeforeEach
    void before() {
        잠실역 = new Station("잠실역");
        강남역 = new Station("강남역");
        section = new Section(잠실역, 강남역, 10);
    }

    @Test
    void equalsStationTest() {
        assertThat(section.isEqualsUpStation(잠실역)).isTrue();
    }
}
