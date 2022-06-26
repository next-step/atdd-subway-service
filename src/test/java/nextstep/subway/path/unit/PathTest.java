package nextstep.subway.path.unit;

import nextstep.subway.UnitTest;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.path.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PathTest extends UnitTest {
    @LocalServerPort
    int port;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private List<Line> allLines = Arrays.asList(신분당선, 이호선, 삼호선);

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

        삼호선.getSections().add(new Section(교대역, 남부터미널역, new Distance(3)));
        삼호선.getSections().add(new Section(양재역, 교대역, new Distance(3)));
        신분당선.getSections().add(new Section(강남역, 양재역, new Distance(7)));
    }

    /**
     * 강남역 -> 남부터미널역 최단거리를 조회한다.
     */
    @Test
    @DisplayName("최단 경로 조회 단위 테스트")
    void 최단경로조회_테스트() {
        // when
        ShortestPathResponse response = PathFinder.findShortestPath(allLines, 강남역, 남부터미널역);
        List<String> answerPaths = Arrays.asList("강남역", "교대역", "남부터미널역");

        // then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(13),
                () -> assertThat(
                        response.getStations()
                                .getStations()
                                .stream()
                                .map(StationResponse::getName)
                                .collect(Collectors.toList())
                                .containsAll(answerPaths)
                ).isTrue()
        );
    }

    /**
     * 출발역, 도착역 동일할 시 최단 거리 조회 예외 테스트
     */
    @Test
    @DisplayName("출발역, 도착역 동일 예외 테스트")
    void 최단경로조회_출발역_도착역_동일_예외() {
        assertThatThrownBy(
                () -> PathFinder.findShortestPath(allLines, 강남역, 강남역)
        ).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("[ERROR]");
    }
}
