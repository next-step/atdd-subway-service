package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.infrastructure.InMemoryLineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationFinder;
import nextstep.subway.station.infrastructure.InMemoryStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 최단 경로 조회 로직")
public class PathServiceTest {
    private PathService pathService;

    @BeforeEach
    void setUp() {
        StationFinder stationFinder = new StationFinder(new InMemoryStationRepository());
        LineRepository lineRepository = new InMemoryLineRepository();

        pathService = new PathService(stationFinder, lineRepository);
    }

    @Test
    void 최단_경로를_조회한다() {
        // when
        PathResponse response = pathService.findShortestPath(1L, 4L);

        // then
        assertThat(response.getDistance()).isEqualTo(9);
    }
}
