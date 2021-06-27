package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;


    private List<Line> lines;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        Line 이호선 = new Line("이호선" ,"RED");
        Line 삼호선 = new Line("삼호선", "GREEN");
        Line 신분당선 = new Line("신분당선", "BLUE");

        이호선.addSection(교대역, 강남역, 10);
        삼호선.addSection(남부터미널역, 양재역, 2);
        삼호선.addSection(교대역, 남부터미널역, 3);
        신분당선.addSection(강남역, 양재역, 10);

        this.lines = Arrays.asList(이호선, 삼호선, 신분당선);
    }


    @DisplayName("지하철 노선, 최단 경로 찾기 테스트")
    @Test
    void getShortestPathsTest() {
        // when
        Paths paths = PathFinder.of(lines).getShortestPaths(남부터미널역, 강남역);

        // then
        assertThat(paths.getShortestStationRoutes())
            .isNotEmpty()
            .containsExactly(남부터미널역, 양재역, 강남역);
        assertThat(paths.getTotalDistance()).isEqualTo(12);
    }
}
