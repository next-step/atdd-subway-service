package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("경로 구간들에서 특정 지하철역들에 대한 정렬된 구간들을 찾을 수 있다.")
    @Test
    void findSections() {
        // given
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());
        PathStation 교대역 = new PathStation(2L, "교대역", LocalDateTime.now());
        PathStation 잠실역 = new PathStation(3L, "잠실역", LocalDateTime.now());

        PathSection pathSection1 = new PathSection(강남역, 교대역, 5);
        PathSection pathSection2 = new PathSection(교대역, 잠실역, 10);

        PathSections pathSections = new PathSections(Arrays.asList(pathSection1, pathSection2));

        // when
        PathStations pathStations = new PathStations(Arrays.asList(교대역, 잠실역));
        PathSections actual = pathSections.findSections(pathStations);

        // then
        assertThat(actual.getPathStations()).containsExactly(교대역, 잠실역);
    }

    @DisplayName("경로 구간들에 등록된 구간과 반대로 역을 검색할 경우 구간의 상행과 하행을 바꾸어 반환한다.")
    @Test
    void findSectionsWithReverse() {
        // given
        PathStation 강남역 = new PathStation(1L, "강남역", LocalDateTime.now());
        PathStation 교대역 = new PathStation(2L, "교대역", LocalDateTime.now());
        PathStation 잠실역 = new PathStation(3L, "잠실역", LocalDateTime.now());

        PathSection pathSection1 = new PathSection(강남역, 교대역, 5);
        PathSection pathSection2 = new PathSection(교대역, 잠실역, 10);

        PathSections pathSections = new PathSections(Arrays.asList(pathSection1, pathSection2));

        // when
        PathStations pathStations = new PathStations(Arrays.asList(잠실역, 교대역));
        PathSections actual = pathSections.findSections(pathStations);

        // then
        assertThat(actual.getPathSections()).containsExactly(new PathSection(잠실역, 교대역, 10));
        assertThat(actual.getPathStations()).containsExactly(잠실역, 교대역);
    }
}
