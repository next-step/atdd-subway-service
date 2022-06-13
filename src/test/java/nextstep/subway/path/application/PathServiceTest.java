package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        //given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @DisplayName("교대역에서 양재역까지 최단 경로 조회를 요청하면, 최단 경로가 조회된다.")
    @Test
    void findShortestPath() {
        //when
        PathResponse pathResponse = pathService.findShortestPath(교대역.getId(), 양재역.getId());

        //then
        List<Station> stations = Arrays.asList(교대역, 남부터미널역, 양재역);
        경유지_확인(pathResponse, stations);
        경유거리_확인(pathResponse, 5);
    }

    private void 경유거리_확인(PathResponse pathResponse, int expectedDistance) {
        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);
    }

    private void 경유지_확인(PathResponse pathResponse, List<Station> expectedStations) {
        List<Station> actualStations = pathResponse.getStations().stream()
                .map(StationResponse::toStation)
                .collect(Collectors.toList());
        assertThat(actualStations).hasSameElementsAs(expectedStations);
    }
}
