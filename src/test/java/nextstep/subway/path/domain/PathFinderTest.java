package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PathFinder 단위 테스트")
class PathFinderTest {

    private Station 계양역;
    private Station 귤현역;
    private Station 김포공항역;
    private Station 마곡나루역;
    private Station 서울역;

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        계양역 = new Station("계양역");
        귤현역 = new Station("귤현역");
        김포공항역 = new Station("김포공항역");
        마곡나루역 = new Station("마곡나루역");
        서울역 = new Station("서울역");

        Line 인천1호선 = new Line("인천1호선", "color", 계양역, 귤현역, 9);
        Line 공항철도 = new Line("공항철도", "color", 김포공항역, 계양역, 66);
        공항철도.addSection(new Section(인천1호선, 마곡나루역, 김포공항역, 23));

        List<Line> lines = Lists.newArrayList(인천1호선, 공항철도);

        pathFinder = new PathFinder(lines);
    }

    @DisplayName("출발역과 도착역이 같으면 오류 발생")
    @Test
    void shortestPathFail01() {

        assertThatExceptionOfType(NotFoundPathException.class).isThrownBy(
            () -> pathFinder.findShortestPath(서울역, 서울역)
        );
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않으면 오류 발생")
    @Test
    void shortestPathFail02() {
        assertThatExceptionOfType(NotFoundPathException.class).isThrownBy(
            () -> pathFinder.findShortestPath(서울역, 계양역)
        );
    }

    @DisplayName("경로 탐색 성공 - 같은 노선")
    @Test
    void shortestPathSuccess01() {

        // when
        ShortestPath shortestPath = pathFinder.findShortestPath(귤현역, 계양역);

        // then
        assertThat(shortestPath.getPath()).containsExactly(귤현역, 계양역);
        assertThat(shortestPath.getDistance()).isEqualTo(9);
    }

    @DisplayName("경로 탐색 성공 - 다른 노선")
    @Test
    void shortestPathSuccess02() {

        // when
        ShortestPath shortestPath = pathFinder.findShortestPath(귤현역, 마곡나루역);

        // then
        assertThat(shortestPath.getPath()).containsExactly(귤현역, 계양역, 김포공항역, 마곡나루역);
        assertThat(shortestPath.getDistance()).isEqualTo(98);
    }
}
