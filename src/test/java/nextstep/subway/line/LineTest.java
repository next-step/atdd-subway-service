package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineTest {

    @DisplayName("라인 생성 검증")
    @Test
    void createLine() {
        Line line = new Line(1L, "7호선", "bg-red-600");

        assertThat(line).isNotNull();
    }

    @DisplayName("라인에 있는 지하철 목록 조회 검증")
    @Test
    void getStations() {
        Station upStation = new Station(1L, "용마산역");
        Station downStation = new Station(2L, "중곡역");
        Line line = new Line("7호선", "bg-red-600", upStation, downStation, 10);

        List<Station> stations = line.getSortedStations();

        assertAll(
                () -> assertThat(stations).isNotNull(),
                () -> assertThat(stations).isEqualTo(Arrays.asList(upStation, downStation))
        );
    }

}
