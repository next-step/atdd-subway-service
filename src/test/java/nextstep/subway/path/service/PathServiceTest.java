package nextstep.subway.path.service;

import nextstep.subway.auth.domain.User;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static nextstep.subway.TestFixture.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 서비스테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    private Line 육호선 = new Line("6호선", "갈색", 연신내역, 응암역, 5, 600);
    private Line 삼호선 = new Line("3호선", "주황색", 연신내역, 불광역, 5, 300);
    private static final Long SOURCE_STATION_ID = 1L;
    private static final Long TARGET_STATION_ID = 2L;

    /*
     *       연신내역ㅡ(5)ㅡ불광역
     *          \         /
     *           (5)    (100)
     *             \    /
     *              응암역
     *
     * */

    @BeforeEach
    void setUp() {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(SOURCE_STATION_ID, 불광역);
        stations.put(TARGET_STATION_ID, 응암역);

        when(stationService.findStations(SOURCE_STATION_ID, TARGET_STATION_ID)).thenReturn(stations);

        육호선.addSection(new Section(육호선, 응암역, 불광역, 100));

        List<Line> lines = new ArrayList<>(Arrays.asList(삼호선, 육호선));
        when(lineRepository.findAll()).thenReturn(lines);
    }

    @DisplayName("최단경로를 찾는다")
    @Test
    void 최단경로_조회() {
        Long 출발역_아이디 = 1L; //불광역
        Long 도착역_아이디 = 2L; //응암역

        PathResponse response = pathService.findShortestPath(new User(), 출발역_아이디, 도착역_아이디);

        assertThat(response.getStations()).hasSize(3); //불광역 --> 연신내역 --> 응암역
        assertThat(response.getStations()).extracting("name")
                .containsExactly("불광역", "연신내역", "응암역");
        assertThat(response.getDistance()).isEqualTo(10);

    }
}
