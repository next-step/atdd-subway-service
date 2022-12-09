package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    Station 삼전역 = new Station("삼전역");
    Station 종합운동장역 = new Station("종합운동장역");
    Station 잠실새내역 = new Station("잠실새내역");

    List<Section> sections;

    @BeforeEach
    void setUp() {
        sections = Arrays.asList(
                new Section(null, 삼전역, 종합운동장역, 5),
                new Section(null, 종합운동장역, 잠실새내역, 5),
                new Section(null, 삼전역, 잠실새내역, 2)
        );
    }

    @Test
    @DisplayName("다익스트라로 최단 경로를 찾음")
    void findByDijkstra() {
        List<Station> byDijkstra = PathFinder.of(sections).findByDijkstra(삼전역, 잠실새내역);

        assertThat(byDijkstra).containsSequence(삼전역,잠실새내역);
    }

    @Test
    @DisplayName("KShortets로 최단 경로를 찾음")
    void findByKShortest() {
        List<Station> byKShortest = PathFinder.of(sections).findByKShortest(삼전역, 잠실새내역);

        assertThat(byKShortest).containsSequence(삼전역,잠실새내역);
    }
}