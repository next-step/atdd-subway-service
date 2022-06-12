package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.exception.NotExistException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;

    @Mock
    private StationService stationService;
    @Mock
    private LineService lineService;
    @Mock
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        pathService = new PathService(stationService, lineService, pathFinder);
    }

    @Test
    @DisplayName("경로 조회시 정상 값 반환")
    void searchShortestPath() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line 이호선 = new Line("이호선", "초록색", 강남역, 교대역, Distance.of(5));

        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(교대역);
        when(lineService.findAllLines()).thenReturn(Collections.singletonList(이호선));
        when(pathFinder.getDijkstraPath(강남역, 교대역)).thenReturn(new Path(Arrays.asList(강남역, 교대역), 5));

        // when
        PathResponse pathResponse = pathService.searchShortestPath(1L, 2L);

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(2),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("경로 조회시 역이 존재하지 않는 경우 예외 발생")
    void searchNotExistStationPath() {
        // given
        when(stationService.findStationById(5L)).thenThrow(NotExistException.class);

        // when && then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathService.searchShortestPath(5L, 10L));
    }
}
