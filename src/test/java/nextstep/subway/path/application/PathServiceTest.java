package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static nextstep.subway.Fixture.*;
import static nextstep.subway.station.dto.StationResponse.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @InjectMocks
    PathService pathService;

    @Mock
    private LineRepository lineRepository;

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
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1L);
        양재역 = createStation("양재역", 2L);
        교대역 = createStation("교대역", 3L);
        남부터미널역 = createStation("남부터미널역", 4L);
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(createSection(교대역, 남부터미널역, 3));
    }


    @DisplayName("최단 경로 조회에 성공한다.")
    @Test
    void findShortestPath() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse actual = pathService.findShortestPath(강남역.getId(), 남부터미널역.getId());

        assertThat(actual.getStations()).containsExactly(of(강남역), of(양재역), of(남부터미널역));
        assertThat(actual.getDistance()).isEqualTo(7);
    }
}
