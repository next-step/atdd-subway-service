package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.PathFindingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShortestPathTest {
    @DisplayName("경로에 존재하지 않는 역으로 객체 생성 시도 시 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = { "1:5", "2:5", "1:4" } ,delimiter = ':')
    void findShortestPathFailByNotExistStationTest(Long sourceId, Long destinationId) {
        List<Long> stationIds = Arrays.asList(2L, 3L, 4L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(2L, 3L, 3),
                new SafeSectionInfo(3L, 4L, 4)
        );

        assertThatThrownBy(() -> ShortestPath.of(PathFactory.of(stationIds, safeSectionInfos), sourceId, destinationId))
                .isInstanceOf(PathFindingException.class)
                .hasMessage("경로에 없는 역을 탐색 대상으로 지정할 수 없습니다.");
    }

    @DisplayName("최단 경로의 역 목록을 구할 수 있다.")
    @Test
    void getPathStationsTest() {
        /*
            1 - 2 - 3
                |
                4
        */
        Long sourceId = 1L;
        Long destinationId = 4L;
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4),
                new SafeSectionInfo(2L, 4L, 5)
        );
        ShortestPath path = ShortestPath.of(PathFactory.of(stationIds, safeSectionInfos), sourceId, destinationId);

        List<Long> stations = path.getPathStations();

        assertThat(stations).hasSize(3);
        assertThat(stations.get(1)).isEqualTo(2L);
    }

    @DisplayName("도달 할 수 없는 경로를 탐색할 경우 예외 발생")
    @Test
    void getPathStationsFailTest() {
        /*
            1 - 2 - 3

            4 - 5
        */
        Long sourceId = 1L;
        Long destinationId = 4L;
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4),
                new SafeSectionInfo(4L, 5L, 5)
        );
        ShortestPath path = ShortestPath.of(PathFactory.of(stationIds, safeSectionInfos), sourceId, destinationId);

        assertThatThrownBy(path::getPathStations)
                .isInstanceOf(PathFindingException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }

    @DisplayName("최단 경로의 총 거리를 구할 수 있다.")
    @Test
    void calculateTotalDistanceTest() {
        /*
            1 - 2 - 3
                |
                4
        */
        Long sourceId = 1L;
        Long destinationId = 4L;
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4),
                new SafeSectionInfo(2L, 4L, 5)
        );
        ShortestPath path = ShortestPath.of(PathFactory.of(stationIds, safeSectionInfos), sourceId, destinationId);

        double distance = path.calculateTotalDistance();

        assertThat(distance).isEqualTo(8);
    }

    @DisplayName("도달할 수 없는 경로의 총 거리를 구할 수 없다.")
    @Test
    void calculateTotalDistanceFailTest() {
        /*
            1 - 2 - 3

            4 - 5
        */
        Long sourceId = 1L;
        Long destinationId = 4L;
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4),
                new SafeSectionInfo(4L, 5L, 5)
        );
        ShortestPath path = ShortestPath.of(PathFactory.of(stationIds, safeSectionInfos), sourceId, destinationId);

        assertThatThrownBy(path::calculateTotalDistance)
                .isInstanceOf(PathFindingException.class)
                .hasMessage("경로가 존재하지 않습니다.");
    }
}