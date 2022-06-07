package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("PathService 에 대한 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    @InjectMocks
    private PathService pathService;

    private Station 대림역;
    private Station 구로디지털단지역;
    private Station 신대방역;
    private Line 노선;

    private Station 남구로역;
    private Station 가산디지털단지역;
    private Line 노선_2;
    private Line 노선_3;

    /**
     * 대림역                 ---       *2호선* ---   구로디지털단지역 |
     *           | *7호선*                                          *2호선* |
     * | 남구로역  --- 가산디지털단지역 ---- *독산선* ---   신대방역
     */
    @BeforeEach
    void setUp() {
        대림역 = new Station("대림");
        구로디지털단지역 = new Station("구로디지털단지");
        신대방역 = new Station("신대방");
        노선 = Line.of("2호선", "testColor", 대림역, 신대방역, 10);
        노선.registerSection(대림역, 구로디지털단지역, 5);

        남구로역 = new Station("남구로");
        가산디지털단지역 = new Station("가산디지털단지");
        노선_2 = Line.of("7호선", "testColor2", 대림역, 남구로역, 10);
        노선_2.registerSection(남구로역, 가산디지털단지역, 4);

        노선_3 = Line.of("독산선", "testColor3", 신대방역, 가산디지털단지역, 7);
    }

    @DisplayName("지하철 최단거리 경로를 조회하면 정상적으로 조회되어야 한다")
    @Test
    void shortest_path_test() {
        // given
        when(lineService.findAll())
            .thenReturn(Collections.singletonList(노선));
        when(stationService.findById(any()))
            .thenReturn(대림역)
            .thenReturn(신대방역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(1L, 3L);

        // then
        assertAll(
            () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
            () -> assertThat(toNames(pathResponse.getStations())).containsExactly("대림", "구로디지털단지",
                "신대방")
        );
    }

    @DisplayName("환승역이 있는 경우 최단경로를 조회하면 정상적으로 조회되어야 한다")
    @Test
    void shortest_path_test2() {
        // given
        when(lineService.findAll())
            .thenReturn(Arrays.asList(노선, 노선_2, 노선_3));
        when(stationService.findById(any()))
            .thenReturn(구로디지털단지역)
            .thenReturn(가산디지털단지역);

        // when
        PathResponse pathResponse = pathService.findShortestPath(2L, 5L);

        // then
        assertAll(
            () -> assertThat(pathResponse.getDistance()).isEqualTo(12),
            () -> assertThat(toNames(pathResponse.getStations()))
                .containsExactly("구로디지털단지", "신대방", "가산디지털단지")
        );
    }


    private List<String> toNames(List<StationResponse> stationResponses) {
        return stationResponses.stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());
    }
}
