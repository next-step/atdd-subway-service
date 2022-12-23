package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Station 선정릉;
    private Station 한티;
    private Line 분당선;

    @BeforeEach
    void setUp() {
        선정릉 = new Station(1L, "선정릉");
        한티 = new Station(2L, "한티");
        분당선 = new Line("분당선", "yellow", 선정릉, 한티, 10);
    }

    @DisplayName("지하철 노선에 해당하는 역 가져오기")
    @Test
    void getStations() {
        List<Station> stations = 분당선.getStations();

        assertThat(stations).isEqualTo(Arrays.asList(선정릉, 한티));
    }

    @DisplayName("지하철 노선에 역 추가하기")
    @Test
    void addSection() {
        Station 선릉 = new Station("선릉");

        분당선.addSection(선정릉, 선릉, 3);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).isEqualTo(Arrays.asList(선정릉, 선릉, 한티));
    }

    @DisplayName("지하철 노선에서 역 삭제하기")
    @Test
    void removeLineStation() {
        Station 선릉 = new Station(3L, "선릉");
        분당선.addSection(선정릉, 선릉, 3);

        분당선.removeLineStation(1L);

        List<Station> stations = 분당선.getStations();
        assertThat(stations).isEqualTo(Arrays.asList(선릉, 한티));
    }
}
