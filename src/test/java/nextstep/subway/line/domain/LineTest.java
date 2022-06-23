package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선")
class LineTest {
    private Station 강남역;
    private Station 역삼역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = Station.from("강남역");
        역삼역 = Station.from("역삼역");

        이호선 = new Line("2호선", "green", 강남역, 역삼역, 5);
    }

    @Test
    void 생성() {
        assertThat(Line.of("2호선", "green")).isNotNull();
    }

    @Test
    @DisplayName("노선에 포함된 모든 역을 조회할 수 있다.")
    void 노선_역_조회() {
        assertThat(이호선.getStations()).containsExactlyInAnyOrder(강남역, 역삼역);
    }

    @Test
    @DisplayName("노선에 역을 추가할 수 있다.")
    void 노선_역_추가() {
        Station 선릉역 = Station.from("선릉역");
        Section section = Section.of(이호선, 역삼역, 선릉역, 4);

        이호선.addLineStation(section);
        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역, 선릉역);
    }
}
