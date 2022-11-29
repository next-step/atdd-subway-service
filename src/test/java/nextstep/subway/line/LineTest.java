package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class LineTest {
    private final String 수인분당선 = "수인분당선";
    private final String 신분당선 = "신분당선";
    private final String YELLOW = "yellow";
    private final String RED = "red";
    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final Station 판교역 = new Station("판교역");
    private final Distance TEN = Distance.from(10);
    private final Distance FIVE = Distance.from(5);

    @Test
    void 생성() {
        Line line = Line.of(신분당선, RED);
        assertAll(
                () -> assertThat(line.getName()).isEqualTo(신분당선),
                () -> assertThat(line.getColor()).isEqualTo(RED)
        );
    }

    @Test
    void 노선_수정() {
        Line line = Line.of(신분당선, RED);
        line.update(수인분당선, YELLOW);
        assertAll(
                () -> assertThat(line.getName()).isEqualTo(수인분당선),
                () -> assertThat(line.getColor()).isEqualTo(YELLOW)
        );
    }

    @Test
    void 노선_추가() {
        Line line = Line.of(신분당선, RED);
        line.addSection(강남역, 광교역, TEN);

        assertThat(line.getSectionsSize()).isEqualTo(1);
    }

    @Test
    void 노선_제거() {
        Line line = Line.of(신분당선, RED);
        line.addSection(강남역, 광교역, TEN);
        line.addSection(강남역, 판교역, FIVE);
        line.removeStation(판교역);

        assertThat(line.getSectionsSize()).isEqualTo(1);
    }

    @Test
    void 노선에_포함된_역_조회() {
        Line line = Line.of(신분당선, RED);
        line.addSection(강남역, 광교역, TEN);
        line.addSection(강남역, 판교역, FIVE);

        List<Station> stations = line.getStations();

        assertAll(
                () -> assertThat(stations.get(0)).isEqualTo(강남역),
                () -> assertThat(stations.get(1)).isEqualTo(판교역),
                () -> assertThat(stations.get(2)).isEqualTo(광교역)
        );
    }
}