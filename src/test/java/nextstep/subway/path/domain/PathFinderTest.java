package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(pathFinder.sectionSize()).isEqualTo(2);
        assertThat(pathFinder.stationSize()).isEqualTo(3);
    }
}