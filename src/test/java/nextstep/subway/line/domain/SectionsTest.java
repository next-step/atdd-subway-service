package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

    private Station 잠실역;
    private Station 선릉역;
    private Station 교대역;
    private Station 당산역;
    private Station 시청역;
    private final Sections sections = new Sections();

    @BeforeEach
    void setUp() {
        잠실역 = new Station("잠실역");
        선릉역 = new Station("선릉역");
        교대역 = new Station("교대역");
        당산역 = new Station("당산역");
        시청역 = new Station("시청역");
        Line line = new Line();

        sections.add(new Section(line, 교대역, 당산역, 100));
        sections.add(new Section(line, 잠실역, 선릉역, 10));
        sections.add(new Section(line, 당산역, 시청역, 30));
        sections.add(new Section(line, 선릉역, 교대역, 10));
    }

    @DisplayName("지하철 노선 역 목록을 가져온다.")
    @Test
    void getStations() {
        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(잠실역, 선릉역, 교대역, 당산역, 시청역);
    }

    @DisplayName("지하철 노선에 구간이 존재하지 않을경우 빈 역 목록을 가져온다.")
    @Test
    void getEmptyStations() {
        // given
        Sections sections = new Sections();

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).isEmpty();
    }

}
