package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.RestAssuredTest;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class PathServiceTest extends RestAssuredTest {

    @Autowired
    PathService pathService;

    @Autowired
    StationRepository stationRepository;

    @MockBean
    PathFindService mockPathFindService;

    private Station 강남역;
    private Station 광교역;
    private Station 을지로3가역;

    @BeforeEach
    public void setUp(){
        super.setUp();
        강남역 = stationRepository.save(new Station("강남역"));
        광교역 = stationRepository.save(new Station("광교역"));
        을지로3가역 = stationRepository.save(new Station("을지로3가역"));
    }


    @Test
    void 최단경로를_조회한다(){
        // Given
        List<Station> shortestPathStations = Lists.newArrayList(강남역, 을지로3가역, 광교역);
        when(mockPathFindService.findShortestPath(강남역,광교역))
                .thenReturn(new PathFindResult(shortestPathStations,10));

        // When
        PathResponse shortestPath = pathService.findShortestPath(강남역.getId(), 광교역.getId());

        // Then
        assertThat(shortestPath.getStations())
                .hasSize(3)
                .containsExactlyElementsOf(StationResponse.of(shortestPathStations));
    }

}
