package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

    private Station 잠실역;
    private Station 삼성역;
    private Station 선릉역;
    private Station 교대역;
    private Station 강남역;
    private Line line;

    @BeforeEach
    void setUp() {
        잠실역 = new Station("잠실역");
        삼성역 = new Station("삼성역");
        선릉역 = new Station("선릉역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");

        line = new Line();
        line.getSections().add(new Section(line, 선릉역, 교대역, 5));
        line.getSections().add(new Section(line, 교대역, 강남역, 5));
        line.getSections().add(new Section(line, 잠실역, 삼성역, 5));
        line.getSections().add(new Section(line, 삼성역, 선릉역, 5));
    }

    @DisplayName("지하철 노선 역 목록을 가져온다.")
    @Test
    void getStations() {
        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).containsExactly(잠실역, 삼성역, 선릉역, 교대역, 강남역);
    }

    @DisplayName("지하철 노선에 구간이 존재하지 않을경우 빈 역 목록을 가져온다.")
    @Test
    void getEmptyStations() {
        // given
        Line line = new Line();

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).isEmpty();
    }
}
