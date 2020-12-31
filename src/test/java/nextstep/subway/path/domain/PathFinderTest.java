package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.PathFindingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    @DisplayName("역ID 컬렉션, SafeSectionInfo 컬렉션을 인자로 받아서 객체를 생성할 수 있다.")
    @Test
    void createTest() {
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4)
        );

        PathFinder pathFinder = PathFinder.of(stationIds, safeSectionInfos);

        assertThat(pathFinder).isNotNull();
    }

    @DisplayName("최단 경로를 찾을 수 있다.")
    @Test
    void findShortestPathTest() {
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
        PathFinder pathFinder = PathFinder.of(stationIds, safeSectionInfos);

        List<Long> shortestPath = pathFinder.findShortestPath(sourceId, destinationId);

        assertThat(shortestPath.get(0)).isEqualTo(1L);
        assertThat(shortestPath.get(1)).isEqualTo(2L);
        assertThat(shortestPath.get(2)).isEqualTo(4L);
    }

    @DisplayName("갈 수 없는 경로를 탐색하면 예외가 발생한다.")
    @Test
    void findShortestPathFailByCannotGoTest() {
        /*
            1 - 2 - 3

            4 - 5
        */
        Long sourceId = 1L;
        Long destinationId = 5L;
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4),
                new SafeSectionInfo(4L, 5L, 5)
        );
        PathFinder pathFinder = PathFinder.of(stationIds, safeSectionInfos);

        assertThatThrownBy(() -> pathFinder.findShortestPath(sourceId, destinationId))
                .isInstanceOf(PathFindingException.class);
    }
}