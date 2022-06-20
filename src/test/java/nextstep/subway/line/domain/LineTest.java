package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    Line 신분당선;
    Station 정자역;
    Station 강남역;

    @BeforeEach
    void setUp() {
        정자역 = new Station("정자역");
        강남역 = new Station("강남역");
        신분당선 = new Line("신분당선", "bg-red-600", 정자역, 강남역, 10000);
    }

    @Test
    @DisplayName("지하철 노선 생성 시 역 정보가 셋팅된다.")
    void createLine() {
        assertThat(신분당선.getStations())
                .containsExactly(정자역, 강남역);
    }

    @Test
    @DisplayName("지하철 역 정보를 추가할 수 있다.")
    void addLineStation() {
        Station 신사역 = new Station("신사역");
        신분당선.addLineStation(강남역, 신사역, 20000);

        assertThat(신분당선.getStations())
                .containsExactly(정자역, 강남역, 신사역);
    }

    @Test
    @DisplayName("지하철 역을 삭제할 수 있다.")
    void removeLineStation() {
        Station 신사역 = new Station("신사역");
        신분당선.addLineStation(강남역, 신사역, 10000);
        신분당선.removeLineStation(강남역);

        assertThat(신분당선.getStations())
                .containsExactly(정자역, 신사역);
    }

    @Test
    @DisplayName("지하철 역을 순서대로 정렬한다.")
    void getStations() {
        Station 광교역 = new Station("광교역");
        신분당선.addLineStation(광교역, 정자역, 10000);

        assertThat(신분당선.getStations())
                .containsExactly(광교역, 정자역, 강남역);
    }
}
