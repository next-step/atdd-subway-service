package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class PathTest {

    @DisplayName("경로 생성")
    @Test
    void init() {
        // given
        int distance = 0;
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "화곡역"));
        stations.add(new Station(2L, "까치산역"));
        stations.add(new Station(3L, "목동역"));

        // when
        Path path = new Path(stations, distance);

        // then
        assertThat(path).isNotNull();
    }
}
