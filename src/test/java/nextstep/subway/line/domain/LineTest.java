package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

    private Station 잠실역;
    private Station 선릉역;
    private Station 교대역;
    private Station 당산역;
    private Station 시청역;
    private Line line;

    @BeforeEach
    void setUp() {
        잠실역 = new Station("잠실역");
        선릉역 = new Station("선릉역");
        교대역 = new Station("교대역");
        당산역 = new Station("당산역");
        시청역 = new Station("시청역");

        line = new Line();
        line.getSections().add(new Section(line, 교대역, 당산역, 100));
        line.getSections().add(new Section(line, 잠실역, 선릉역, 10));
        line.getSections().add(new Section(line, 당산역, 시청역, 30));
        line.getSections().add(new Section(line, 선릉역, 교대역, 10));
    }

    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void removeSection() {
        // when
        line.removeLineStation(잠실역);
        line.removeLineStation(교대역);
        line.removeLineStation(시청역);

        // then
        assertThat(line.getStations()).containsExactly(선릉역, 당산역);
    }

    @DisplayName("존재하지 않는 구간은 삭제할 수 없다.")
    @Test
    void removeNotExistSection() {
        // given
        Line line = new Line();

        // when & then
        assertThrows(RuntimeException.class, () ->
            line.removeLineStation(new Station("신촌역")));
    }
}
