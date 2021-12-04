package nextstep.subway.path;

import nextstep.subway.path.ui.application.PathFinder;
import nextstep.subway.path.ui.application.PathService;
import nextstep.subway.path.ui.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * packageName : nextstep.subway.path
 * fileName : PathMockitoExtensionTest
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@ExtendWith(MockitoExtension.class)
public class PathMockitoExtensionTest {

    @Mock
    private PathFinder finder;

    @Test
    @DisplayName("경로 조회")
    void findPaths() {
        // given
        Long startId = 1L;
        Long targetId = 2L;

        when(finder.getPath())
                .thenReturn(PathResponse.of(
                        Arrays.asList(
                                StationResponse.of(new Station("강남역")),
                                StationResponse.of(new Station("역삼역")))));

        PathService pathService = new PathService(finder);

        // when
        PathResponse response = pathService.getPath(startId, targetId);

        // then
        assertThat(response.getStations()).hasSize(2);

    }
}
