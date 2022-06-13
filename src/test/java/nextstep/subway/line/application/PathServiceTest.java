package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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

    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        Line 이호선 = new Line("이호선", "초록색", 강남역, 양재역, 5);

        when(lineService.findLines()).thenReturn(Lists.newArrayList(이호선));

        pathService = new PathService(stationService, lineService, pathFinder);
    }

    @Test
    void 최단_경로_조회() {
        // given
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(pathFinder.findShortestPath(강남역, 양재역)).thenReturn(Path.of(Arrays.asList(강남역, 양재역), 5));

        // when
        PathResponse response = pathService.findShortestPath(1L, 2L);

        // then
        List<String> stationNames = response.getStations().stream().map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(5),
                () -> assertThat(stationNames).containsExactly(강남역.getName(), 양재역.getName())
        );
    }
}