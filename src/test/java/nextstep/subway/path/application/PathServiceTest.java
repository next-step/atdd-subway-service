package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @DisplayName("최단거리를 구한다")
    @Test
    void findPathTest() {
        PathService pathService = new PathService(stationRepository);
        PathResponse pathResponse = pathService.findPath(1L, 2L);

        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList())).containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "남부터미널역"));
    }
}