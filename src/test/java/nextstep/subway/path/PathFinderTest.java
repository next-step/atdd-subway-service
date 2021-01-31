package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.dto.PathFindResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    private Station 교대역;
    private Station 강남역;
    private Station 서초역;
    private Station 역삼역;
    private Line 삼호선;
    private Line 이호선;
    private Line 신분당선;
    private Station 고속터미널역;
    private Station 신사역;
    private Station 양재역;
    private Station 정자역;
    private Station 판교역;
    private Lines lines;

    /**
     * 2호선 - 서초 - 교대 - 강남 - 역삼
     * 3호선 - 신사 - 고터 - 교대 - 양재
     * 신분당선 - 양재 - 강남 - 정자 - 판교
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        교대역 = new Station(2L, "교대역");
        서초역 = new Station(3L, "서초역");
        역삼역 = new Station(4L, "역삼역");

        이호선 = new Line("이호선", "green", 서초역, 교대역, 20, 0);
        이호선.addSection(교대역, 강남역, 10);
        이호선.addSection(강남역, 역삼역, 10);

        신사역 = new Station(5L, "신사역");
        고속터미널역 = new Station(6L, "고속터미널역");
        양재역 = new Station(8L, "양재역");

        삼호선 = new Line("삼호선", "orange", 신사역, 고속터미널역, 9, 0);
        삼호선.addSection(고속터미널역, 교대역, 3);
        삼호선.addSection(교대역, 양재역, 3);

        정자역 = new Station(9L, "정자역");
        판교역 = new Station(10L, "판교역");
        신분당선 = new Line("신분당선", "red", 양재역, 강남역, 20, 0);
        신분당선.addSection(강남역, 정자역, 32);
        신분당선.addSection(정자역, 판교역, 199);
        lines = new Lines(new ArrayList<Line>() {{
            add(이호선);
            add(삼호선);
            add(신분당선);
        }});
    }

    @DisplayName("최단 경로 테스트 - 2개라인, 고터 - 교대 - 강남 - 정자 - 판교")
    @Test
    public void shortestPathTest() {
        PathFindResponse pathFindResponse = PathFinder.findPath(lines.getLines(), 고속터미널역, 판교역);
        assertThat(pathFindResponse.getStationIds().size()).isEqualTo(5);

        List<Long> expected = new ArrayList<>(Arrays.asList(6L, 2L, 1L, 9L, 10L));
        assertThat(pathFindResponse.getStationIds()).isEqualTo(expected);
    }

}
