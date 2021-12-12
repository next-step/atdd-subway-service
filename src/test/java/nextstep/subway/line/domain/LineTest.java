package nextstep.subway.line.domain;

import static nextstep.subway.station.StationFixtures.잠실;
import static nextstep.subway.station.StationFixtures.잠실나루;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @Test
    @DisplayName("구간 기본정보 업데이트 후 값 검증")
    void update() {
        // given
        Line line = new Line("2호선", "RED");
        Line updateLine = new Line("3호선", "YELLOW");

        // when
        line.update(updateLine);

        // then
        assertThat(line.isSameNameAndColor(updateLine)).isTrue();
    }

    @Test
    @DisplayName("노선 역 조회 순서 검증")
    void getStations_up_down_ordering() {
        // given
        Line line = new Line("2호선", "RED", 잠실, 잠실나루, 100);

        // when
        // then
        assertThat(line.getStations()).extracting("name")
            .containsExactly(잠실.getName(), 잠실나루.getName());
    }

}
