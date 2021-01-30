package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathTest {

    private static Line 이호선;
    private static Line 삼호선;
    private static Line 신분당선;
    private static Path path;
    private static Station 강남역;
    private static Station 남부터미널역;
    private static Station 양재역;
    private static Station 교대역;

    @BeforeAll
    static void beforeAll() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = Line.builder()
                .name("신분당선")
                .color("빨간색")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .build();

        이호선 = Line.builder()
                .name("이호선")
                .color("초록색")
                .upStation(교대역)
                .downStation(강남역)
                .distance(10)
                .build();

        삼호선 = Line.builder()
                .name("삼호선")
                .color("오렌지색")
                .upStation(교대역)
                .downStation(양재역)
                .distance(5)
                .build();

        Section 교대역_남부터미널역 = Section.builder()
                .line(삼호선)
                .upStation(교대역)
                .downStation(남부터미널역)
                .distance(3)
                .build();

        삼호선.addSection(교대역_남부터미널역);

        path = new Path(Stream.of(신분당선, 삼호선, 이호선)
                .flatMap(line -> line.getAllSection().stream())
                .collect(toList()));
    }

    @DisplayName("지하철역 최단경로 조회")
    @Test
    void selectShortPath() {
        // given when
        ShortestPath shortestPath = path.selectShortestPath(강남역, 남부터미널역);
        // then
        assertThat(shortestPath.getStations())
                .map(Station::getName).containsExactly("강남역", "양재역", "남부터미널역");
    }

    @DisplayName("존재하지 않는 역 조회 시")
    @Test
    void selectNotExistStation() {
        // given
        Station 소요산역 = new Station("소요산역");
        Station 인천역 = new Station("인천역");
        // when then
        assertThrows(IllegalArgumentException.class, () -> {
            path.selectShortestPath(소요산역,인천역);
        });
    }

    @DisplayName("출발역과 도착역이 같을 경우")
    @Test
    void selectSameStation() {
        assertThrows(IllegalArgumentException.class, () -> {
            path.selectShortestPath(강남역, 강남역);
        });
    }
}
