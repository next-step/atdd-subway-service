package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.FakeLineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.FakeStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 최단 경로 조회 로직")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private PathService pathService;

    @BeforeEach
    void setUp() {
        StationService stationService = new StationService(new FakeStationRepository());
        LineRepository lineRepository = new FakeLineRepository();

        pathService = new PathService(stationService, lineRepository);
    }

    @Test
    void 최단_경로를_조회한다() {
        // when
        PathResponse response = pathService.findShortestPath(1L, 4L);

        // then
        assertThat(response.getDistance()).isEqualTo(9);
    }
}
