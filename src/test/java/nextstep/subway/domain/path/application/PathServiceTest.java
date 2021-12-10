package nextstep.subway.domain.path.application;

import nextstep.subway.domain.line.application.LineService;
import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.line.domain.LineRepository;
import nextstep.subway.domain.path.dto.PathFinderRequest;
import nextstep.subway.domain.path.dto.PathFinderResponse;
import nextstep.subway.domain.station.application.StationService;
import nextstep.subway.domain.station.domain.Station;
import nextstep.subway.domain.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @Test
    @DisplayName("최단 경로 조회")
    void findPaths() {
        // given
        final Station 강남역 = new Station(1L, "강남역");
        final Station 양재역 = new Station(2L, "양재역");
        final Station 교대역 = new Station(3L, "교대역");
        final Station 남부터미널역 = new Station(4L, "남부터미널역");
        final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, new Distance(10));
        final Line 이호선 = new Line("이호선", "bg-green-400", 교대역, 강남역, new Distance(10));
        final Line 삼호선 = new Line("삼호선", "bg-orange-200", 교대역, 양재역, new Distance(5));
        삼호선.addSection(교대역, 남부터미널역, new Distance(3));

        when(lineService.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findAll()).thenReturn(Arrays.asList(강남역,양재역,교대역,남부터미널역));

        // when
        final PathFinderRequest pathFinderRequest = new PathFinderRequest(교대역.getId(), 양재역.getId());
        final PathFinderResponse pathFinderResponse = pathService.findPaths(pathFinderRequest);

        // then
        assertAll(() -> {
            assertThat(pathFinderResponse.getStations()).extracting("name").containsExactly("교대역","남부터미널역","양재역");
            assertThat(pathFinderResponse.getDistance()).isEqualTo(5);
        });


    }
}