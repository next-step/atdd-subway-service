package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.util.PathSearch;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private StationService stationService;

    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    @Mock
    private PathSearch pathSearch;

    private Line line1 = new Line("2호선", "green");
    private Line line2 = new Line("3호선", "orange");

    private Station station1 = new Station("강남역");
    private Station station2 = new Station("교대역");
    private Station station3 = new Station("남부터미널역");
    private Station station4 = new Station("양재역");

    private List<StationResponse> stations = Arrays.asList(
            StationResponse.of(station1), StationResponse.of(station2),
            StationResponse.of(station3), StationResponse.of(station4));

    private PathResponse pathResponse = new PathResponse(stations, 20);


    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationService, pathSearch);
    }

    @Test
    void findPath() {
        Long source = 1L;
        Long target = 4L;
        when(stationRepository.findById(anyLong()))
                .thenReturn(Optional.of(station1));
        when(stationRepository.findById(anyLong()))
                .thenReturn(Optional.of(station4));
        when(pathSearch.findPaths(any(Lines.class), any(Station.class), any(Station.class)))
                .thenReturn(pathResponse);

        PathResponse paths = pathService.findPaths(source, target);

        assertThat(paths.getStations()).extracting("name")
                .contains("강남역", "교대역", "남부터미널역", "양재역");
    }

    @DisplayName("동일한 시작 종료역으로 경로 조회시 예외를 던집니다")
    @ParameterizedTest
    @CsvSource(value = {"1, 1", "2, 2", "3, 3"})
    void findPathWithDuplicateSourceAndTarget(Long source, Long target) {
        assertThatThrownBy(() -> pathService.findPaths(source, target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("시작 종료역 미입력 조회시 예외를 던집니다")
    @NullSource
    @ParameterizedTest
    void findPathWithNullStationId(Long stationId) {
        assertThatThrownBy(() -> pathService.findPaths(stationId, stationId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
