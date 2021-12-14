package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@DataJpaTest
public class SectionsTest {
    @DisplayName("노선 생성 후 지하철 목록 조회")
    @Test
    void getStations() {
        //given
        Station upStation = new Station(1L, "1번역");
        Station downStation = new Station(2L, "2번역");
        Line line = new Line("신분당선", "red", upStation, downStation, 10);
        List<Station> stations = line.getSortedStations();

        //when
        Station firstStation = stations.get(0);
        Station lastStation = stations.get(stations.size() - 1);

        //then
        assertThat(firstStation).isEqualTo(upStation);
        assertThat(lastStation).isEqualTo(downStation);
    }
}
