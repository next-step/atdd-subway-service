package nextstep.subway.path.application;

import static nextstep.subway.path.LineFixtures.강남역;
import static nextstep.subway.path.LineFixtures.남부터미널역;
import static nextstep.subway.path.LineFixtures.모든_노선;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundException;
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
    private static final Long 존재하지_않는_역 = -1L;

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

        lenient().when(lineRepository.findAll()).thenReturn(모든_노선);

        lenient().when(stationService.findStationById(eq(출발역.getId()))).thenReturn(출발역);
        lenient().when(stationService.findStationById(eq(도착역.getId()))).thenReturn(도착역);
        lenient().when(stationService.findStationById(eq(존재하지_않는_역))).thenThrow(NotFoundException.class);
    }

    @Test
    @DisplayName("출발역과 도착역의 아이디로 최단 경로를 찾을 수 있다.")
    void 경로_찾기() {
        PathService pathService = new PathService(lineRepository, stationService);
        PathResponse pathResponse = pathService.findShortestPath(new LoginMember(), 출발역.getId(), 도착역.getId());

        assertAll(() -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12));
    }

    @Test
    void 존재하지_않은_출발역이나_도착역으로_경로_찾기() {
        PathService pathService = new PathService(lineRepository, stationService);
        Long 출발역_아이디 = 출발역.getId();

        assertThatThrownBy(() -> pathService.findShortestPath(new LoginMember(), 출발역_아이디, 존재하지_않는_역)).isInstanceOf(
                NotFoundException.class);
    }
}
