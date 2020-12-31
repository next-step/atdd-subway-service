package nextstep.subway.path.application;

import nextstep.subway.path.domain.SafeSectionInfo;
import nextstep.subway.path.domain.SafeStationInfo;
import nextstep.subway.path.domain.adapters.SafeLineAdapter;
import nextstep.subway.path.domain.adapters.SafeStationAdapter;
import nextstep.subway.path.ui.dto.PathResponse;
import nextstep.subway.path.ui.dto.StationInPathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;

    @Mock
    private SafeLineAdapter safeLineAdapter;

    @Mock
    private SafeStationAdapter safeStationAdapter;

    @BeforeEach
    void setup() {
        pathService = new PathService(safeLineAdapter, safeStationAdapter);
    }

    @DisplayName("최단 경로를 찾아서 응답할 수 있다.")
    @Test
    void findShortestPathTest() {
        Long sourceId = 1L;
        Long destinationId = 4L;

        given(safeLineAdapter.getAllStationIds()).willReturn(Arrays.asList(1L, 2L, 3L, 4L));
        given(safeLineAdapter.getAllSafeSectionInfos()).willReturn(Arrays.asList(
                new SafeSectionInfo(1L, 2L, 5),
                new SafeSectionInfo(2L, 3L, 5),
                new SafeSectionInfo(2L, 4L, 5)
        ));
        given(safeStationAdapter.findStationsById(any())).willReturn(Arrays.asList(
                new SafeStationInfo(1L, "강남역", null),
                new SafeStationInfo(2L, "역삼역", null),
                new SafeStationInfo(4L, "삼성역", null)
        ));

        PathResponse pathResponse = pathService.findShortestPath(sourceId, destinationId);

        assertThat(pathResponse.getStations()).contains(
                new StationInPathResponse(1L, "강남역", null),
                new StationInPathResponse(2L, "역삼역", null),
                new StationInPathResponse(4L, "삼성역", null)
        );
        assertThat(pathResponse.getDistance()).isEqualTo(10);
    }
}