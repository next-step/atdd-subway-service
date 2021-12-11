package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.JGraphTPathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @DisplayName("최단거리를 구한다")
    @Test
    void findPathTest() {
        when(stationRepository.findById(1L)).thenReturn(java.util.Optional.of(new Station("강남역")));
        when(stationRepository.findById(2L)).thenReturn(java.util.Optional.of(new Station("남부터미널역")));

        PathService pathService = new PathService(stationRepository, lineRepository, new JGraphTPathFinder());
        PathResponse pathResponse = pathService.findPath(1L, 2L);

        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList())).containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "남부터미널역"));
    }
}