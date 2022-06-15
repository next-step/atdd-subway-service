package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("지하철 최단 경로 조회 로직")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static Station 강남역;
    private static Station 교대역;

    @InjectMocks
    private PathService pathService;
    @Mock
    private StationService stationService;
    @Mock
    private LineRepository lineRepository;

    private List<Line> lines;


    @BeforeEach
    void setUp() {
        lines = getLines();
    }

    public static List<Line> getLines() {
        강남역 = getStation(1L, "강남역");
        Station 양재역 = getStation(2L, "양재역");
        Station 남부터미널역 = getStation(3L, "남부터미널역");
        교대역 = getStation(4L, "교대역");
        Station 사당역 = getStation(5L, "사당역");
        Station 이수역 = getStation(6L, "이수역");

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4);
        Line 삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 3);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 사호선 = new Line("사호선", "blue", 사당역, 이수역, 10);

        삼호선.addSection(new Section(남부터미널역, 양재역, 2));

        return Arrays.asList(신분당선, 삼호선, 이호선, 사호선);
    }

    public static Station getStation(long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);

        return station;
    }

    @Test
    void 최단_경로를_조회한다() {
        // given
        given(lineRepository.findAll()).willReturn(lines);
        given(stationService.findStationById(1L))
                .willReturn(강남역);
        given(stationService.findStationById(4L))
                .willReturn(교대역);

        // when
        PathResponse response = pathService.findShortestPath(1L, 4L);

        // then
        assertThat(response.getDistance()).isEqualTo(9);
    }
}
