package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private PathService pathService;

    private Station 강남역 = initStation("강남역", 1L);
    private Station 양재역 = initStation("양재역", 2L);
    private Station 교대역 = initStation("교대역", 3L);
    private Station 남부터미널역 = initStation("남부터미널역", 4L);

    @DisplayName("최단 경로 조회 테스트")
    @Test
    void findShortestPath() {
        PathRequest pathRequest = new PathRequest(교대역.getId(), 양재역.getId());

        PathResponse shortestPath = pathService.findShortestPath(pathRequest);

        Assertions.assertThat(shortestPath.getStations())
                .containsExactlyElementsOf(Arrays.asList(교대역, 남부터미널역, 양재역));
    }

    private Station initStation(String name, Long id) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}