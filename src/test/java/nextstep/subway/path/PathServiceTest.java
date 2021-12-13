package nextstep.subway.path;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.infra.PathFinderStrategy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private PathFinderStrategy pathFinder;

    @DisplayName("최단 경로 찾기 - 서비스 Layer")
    @Test
    void findShortestPathServiceOutSideInTest() {

        //given
        Long sourceId = 1L;
        Long targetId = 6L;
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "red", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "red", 교대역, 양재역, 5);

        삼호선.addSection(남부터미널역, 양재역, 3);

        List<Line> lines = Arrays.asList(신분당선, 이호선, 삼호선);

        PathService pathService = new PathService(lineRepository, stationService, pathFinder);
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationService.findById(sourceId)).thenReturn(강남역);
        when(stationService.findById(targetId)).thenReturn(남부터미널역);
        when(pathFinder.findStations(anyList(), any(), any())).thenReturn(Arrays.asList(강남역, 교대역, 남부터미널역));
        when(pathFinder.findDistance(anyList(), any(), any())).thenReturn(new Distance(12));

        //when
        PathResponse shortestPath = pathService.findShortestPath(sourceId, targetId);

        //then
        assertThat(shortestPath.getDistance()).isEqualTo(12);
        assertThat(shortestPath.getStations().stream().map(StationResponse::getName))
                .containsExactly("강남역", "교대역", "남부터미널역");

    }

}
