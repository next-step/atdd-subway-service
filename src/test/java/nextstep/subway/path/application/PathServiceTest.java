package nextstep.subway.path.application;

import static nextstep.subway.path.LineFixtures.강남역;
import static nextstep.subway.path.LineFixtures.남부터미널역;
import static nextstep.subway.path.LineFixtures.모든_노선;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("경로 서비스")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    private Station 출발역;
    private Station 도착역;

    @BeforeEach
    void setUp() {
        출발역 = 강남역;
        도착역 = 남부터미널역;

        when(lineRepository.findAll()).thenReturn(모든_노선);

        when(stationService.findStationById(eq(출발역.getId()))).thenReturn(출발역);
        when(stationService.findStationById(eq(도착역.getId()))).thenReturn(도착역);
    }

    @Test
    @DisplayName("출발역과 도착역의 아이디로 최단 경로를 찾을 수 있다.")
    void 경로_찾기() {
        PathService pathService = new PathService(lineRepository, stationService);
        PathResponse pathResponse = pathService.findShortestPath(출발역.getId(), 도착역.getId());

        assertAll(() -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12));
    }
}
