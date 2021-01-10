package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 잠실역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
        잠실역 = new Station(3L, "잠실역");

        신분당선 = new Line(1L, "신분당선", "bg-red-600", 강남역, 역삼역, 10);
    }

    @DisplayName("상행 종점역을 조회함")
    @Test
    void findUpStation() {
        //given
        신분당선.addLineStation(강남역, 잠실역, 4);
        //when
        Station upStation = 신분당선.findUpStation();
        //then
        assertThat(upStation.getId()).isEqualTo(1L);
        assertThat(upStation.getName()).isEqualTo("강남역");
    }
}