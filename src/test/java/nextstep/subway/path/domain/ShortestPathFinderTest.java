package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShortestPathFinderTest {

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;
    Station 공사중인역;

    Line 이호선;
    Line 삼호선;
    Line 신분당선;
    List<Line> lines;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        공사중인역 = new Station("공사중인역");

        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange", 교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);
        신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 10);
        lines = new ArrayList<>();
        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);
    }

    @DisplayName("모든 노선의 구간을 등록한 뒤 최단 경로를 구한다")
    @Test
    void findShortestPath() {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();

        Path shortestPath = shortestPathFinder.findShortestPath(lines, 남부터미널역, 강남역);

        assertAll(
                () -> assertThat(shortestPath.getStations()).containsExactly(남부터미널역, 양재역, 강남역),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(13)
        );
    }

    @DisplayName("출발역과 도착역이 같으면 EX 발생")
    @Test
    void sameSourceAndTarget() {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();

        ThrowingCallable 출발역과_도착역이_같다 = () -> shortestPathFinder.findShortestPath(lines, 남부터미널역, 남부터미널역);

        assertThatIllegalArgumentException().isThrownBy(출발역과_도착역이_같다)
                .withMessageContaining("출발역과 도착역이 같을 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않으면 EX 발생")
    @Test
    void notAddedEdge() {
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();

        ThrowingCallable 출발역과_도착역이_연결되어_있지_않다 = () -> shortestPathFinder.findShortestPath(lines, 공사중인역, 남부터미널역);

        assertThatIllegalArgumentException().isThrownBy(출발역과_도착역이_연결되어_있지_않다)
                .withMessageContaining("출발역과 도착역의 연결정보가 없습니다.");
    }
}
