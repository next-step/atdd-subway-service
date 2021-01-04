package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathSectionsTest {

    @DisplayName("경로 구간들을 생성한다.")
    @Test
    void create() {
        // given
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());
        PathStation 교대역 = new PathStation(2L, "교대역", LocalDateTime.now());
        PathStation 잠실역 = new PathStation(3L, "잠실역", LocalDateTime.now());

        PathSection pathSection1 = new PathSection(강남역, 교대역, 5);
        PathSection pathSection2 = new PathSection(교대역, 잠실역, 10);

        // when
        PathSections pathSections = new PathSections(Arrays.asList(pathSection1, pathSection2));

        // then
        assertThat(pathSections).isNotNull();
    }

    @DisplayName("경로 구간들의 경로 지하철역을 조회한다. ")
    @Test
    void getPathStations() {
        // given
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());
        PathStation 교대역 = new PathStation(2L, "교대역", LocalDateTime.now());
        PathStation 잠실역 = new PathStation(3L, "잠실역", LocalDateTime.now());

        PathSection pathSection1 = new PathSection(강남역, 교대역, 5);
        PathSection pathSection2 = new PathSection(교대역, 잠실역, 10);

        PathSections pathSections = new PathSections(Arrays.asList(pathSection1, pathSection2));

        // when
        List<PathStation> pathStations = pathSections.getPathStations();

        // then
        assertThat(pathStations.size()).isEqualTo(3);
        assertThat(pathStations).contains(강남역, 교대역, 잠실역);
    }

}
