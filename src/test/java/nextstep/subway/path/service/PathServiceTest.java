package nextstep.subway.path.service;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private Line 육호선 = new Line("6호선", "갈색");
    private Line 삼호선 = new Line("3호선", "주황색");
    private Station 연신내역 = new Station("연신내역");
    private Station 불광역 = new Station("불광역");
    private Station 응암역 = new Station("응암역");

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
        when(stationService.findStationById(1L)).thenReturn(불광역);
        when(stationService.findStationById(2L)).thenReturn(응암역);

        삼호선.addSection(new Section(삼호선, 연신내역, 불광역, 5));
        육호선.addSection(new Section(육호선, 연신내역, 응암역, 5));
        육호선.addSection(new Section(육호선, 응암역, 불광역, 100));

        List<Line> lines = new ArrayList<>(Arrays.asList(삼호선, 육호선));
        when(lineRepository.findAll()).thenReturn(lines);
    }

    @DisplayName("최단경로를 찾는다")
    @Test
    void 최단경로_조회() {
        Long 출발역_아이디 = 1L; //불광역
        Long 도착역_아이디 = 2L; //응암역

        PathResponse response = pathService.findShortestPath(출발역_아이디, 도착역_아이디);

        assertThat(response.getPathStations()).hasSize(3); //불광역 --> 연신내역 --> 응암역
        assertThat(response.getPathStations()).extracting("name")
                .containsExactly("불광역", "연신내역", "응암역");
        assertThat(response.getDistance()).isEqualTo(10);

    }
}
