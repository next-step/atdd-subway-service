package nextstep.subway.path.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.StationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class PathServiceTest {

    @MockBean
    private LineRepository lineRepository;

    @MockBean
    private StationRepository stationRepository;

    @InjectMocks
    private PathService pathService;

    private Line 이호선;
    private Line 일호선;

    void setUp() {
        이호선 = new Line("이호선", "green", StationTest.STATION_4, StationTest.STATION_5, 10);
        이호선.addStation(StationTest.STATION_3, StationTest.STATION_5, 4);
        일호선 = new Line("일호선", "navy", StationTest.STATION_4, StationTest.STATION_5, 2);
    }

    @Test
    public void find_shortest_path() {
        // given
        setUp();
        Long source = 1L;
        Long target = 6L;

        // when
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 이호선));
        when(stationRepository.findById(source)).thenReturn(java.util.Optional.of(StationTest.STATION_4));
        when(stationRepository.findById(target)).thenReturn(java.util.Optional.of(StationTest.STATION_5));
        PathResponse response = pathService.findShortestPath(source, target);

        // then
        assertThat(response.getDistance()).isEqualTo(2);
        assertThat(response.getStations().contains(StationTest.STATION_4));
        assertThat(response.getStations().contains(StationTest.STATION_5));
    }

}