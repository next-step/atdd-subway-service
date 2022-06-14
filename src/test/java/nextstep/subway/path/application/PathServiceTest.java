package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    StationRepository stationRepository;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

        new Section(삼호선, 교대역, 남부터미널역, 3);
    }

    /*
     * Given : 다양한 노선에 구간들이 저장 되어 있고
     * When : 등록된 구간들 중에서 시작역과 도착역을 찾으면
     * Then : 최단 경로 와 거리를 알수 있다.
     */
    @DisplayName("시작역과 도착역의 정보를 이용하여 최단 경로와 거리를 알수있다.")
    @Test
    void findShortestRouteTest() {
        when(stationRepository.findById(3L)).thenReturn(Optional.ofNullable(교대역));
        when(stationRepository.findById(2L)).thenReturn(Optional.ofNullable(양재역));

        final PathService pathService = new PathService(stationRepository);

        // when
        final PathResponse pathResponse = pathService.findShortestRoute(교대역.getId(), 양재역.getId());

        // then
        최단_경로_정보를_가져옴(pathResponse, Arrays.asList(양재역, 남부터미널역, 교대역));
    }

    private void 최단_경로_정보를_가져옴(PathResponse pathResponse, List<Station> expectedResult) {
        final List<String> resultStationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        final List<String> expectedResultNames = expectedResult.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(resultStationNames).containsExactlyElementsOf(expectedResultNames);
    }
}