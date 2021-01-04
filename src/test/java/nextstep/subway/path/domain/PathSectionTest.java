package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathSectionTest {

    @DisplayName("경로 지하철 구간을 생성한다.")
    @Test
    void create() {
        // given
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());
        PathStation 교대역 = new PathStation(2L, "교대역", LocalDateTime.now());

        // when
        PathSection pathSection = new PathSection(강남역, 교대역, 10);

        // then
        assertThat(pathSection).isNotNull();
    }

    @DisplayName("경로 지하철 역을 조회한다.")
    @Test
    void getPathStations() {
        // given
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());
        PathStation 교대역 = new PathStation(2L, "교대역", LocalDateTime.now());

        // when
        PathSection pathSection = new PathSection(강남역, 교대역, 10);
        List<PathStation> stations = pathSection.getStations();

        // then
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations).contains(강남역, 교대역);
    }
}
