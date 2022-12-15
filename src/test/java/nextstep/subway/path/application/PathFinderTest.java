package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    Station 삼전역 = new Station("삼전역");
    Station 종합운동장역 = new Station("종합운동장역");
    Station 잠실새내역 = new Station("잠실새내역");

    Line line;

    List<Section> sections;

    PathFindAlgorithm dijkstraAlgorithm;
    KShortestPathFinder kShortestAlgorithm;


    @BeforeEach
    void setUp() {
        line = new Line("노선", "색상", 삼전역, 종합운동장역, 5, 500);
        sections = Arrays.asList(
            new Section(line, 삼전역, 종합운동장역, 5),
            new Section(line, 종합운동장역, 잠실새내역, 5),
            new Section(line, 삼전역, 잠실새내역, 2)
        );
        dijkstraAlgorithm = new DijkstraPathFinder();
        kShortestAlgorithm = new KShortestPathFinder();
    }

    @Test
    @DisplayName("다익스트라로 최단 경로를 찾음")
    void findByDijkstra() {
        List<Station> byDijkstra = PathFinder.of(sections, dijkstraAlgorithm).find(삼전역, 잠실새내역);

        assertThat(byDijkstra).containsSequence(삼전역, 잠실새내역);
    }

    @Test
    @DisplayName("KShortets로 최단 경로를 찾음")
    void findByKShortest() {
        List<Station> byKShortest = PathFinder.of(sections, kShortestAlgorithm).find(삼전역, 잠실새내역);

        assertThat(byKShortest).containsSequence(삼전역, 잠실새내역);
    }

    @Test
    @DisplayName("경로를 찾을 수 없는 경우")
    void findByDijkstra2() {
        Station 동대문역 = new Station("동대문역");
        Station 충무로역 = new Station("충무로역");
        List<Section> notConnectedSections = Arrays.asList(
            new Section(line, 삼전역, 종합운동장역, 5),
            new Section(line, 동대문역, 충무로역, 5)
        );

        assertThatThrownBy(() -> PathFinder.of(notConnectedSections, dijkstraAlgorithm).find(삼전역, 충무로역))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
