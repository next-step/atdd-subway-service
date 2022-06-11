package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class PathServiceTest {

    @MockBean
    StationRepository mockStationRepository;

    @MockBean
    LineRepository mockLineRepository;

    @MockBean
    PathFindService mockPathFindService;

    PathService pathService;

    private Station 강남역 = new Station("강남역");
    private Station 광교역 = new Station("광교역");
    private Station 을지로3가역 = new Station("을지로3가역");
    private WeightedMultigraph<Station, SectionEdge> mockGraph = new WeightedMultigraph<>(SectionEdge.class);

    @BeforeEach
    public void setUp() {
        pathService = new PathService(mockStationRepository, mockLineRepository, mockPathFindService);
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(광교역, "id", 2L);
    }

    @Test
    void 최단경로를_조회한다() throws Exception {
        // Given
        List<Station> shortestPathStations = Lists.newArrayList(강남역, 을지로3가역, 광교역);

        when(mockPathFindService.findShortestPath(mockGraph, 강남역, 광교역))
                .thenReturn(new PathFindResult(shortestPathStations, 10));
        when(mockStationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(mockStationRepository.findById(2L)).thenReturn(Optional.of(광교역));
        // When
        PathResponse shortestPath = pathService.findShortestPath(강남역.getId(), 광교역.getId());

        // Then
        assertThat(shortestPath.getStations())
                .hasSize(3)
                .containsExactlyElementsOf(StationResponse.of(shortestPathStations));
    }

}
