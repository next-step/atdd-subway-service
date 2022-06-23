package nextstep.subway.path.domain;


import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphPathTest {
    private List<Line> lines;
    WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    @BeforeEach
    void setUp() {
        final Line 이호선 = new Line("2호선", "bg-blue-400", new Station("강남역"), new Station("잠실역"), 80);
        이호선.addSection(new Section(이호선, new Station("강남역"), new Station("교대역"), 5));
        이호선.addSection(new Section(이호선, new Station("교대역"), new Station("서초역"), 8));
        이호선.addSection(new Section(이호선, new Station("서초역"), new Station("방배역"), 7));
        이호선.addSection(new Section(이호선, new Station("방배역"), new Station("사당역"), 3));

        final Line 사호선 = new Line("4호선", "bg-blue-400", new Station("사당역"), new Station("용산역"), 50);
        이호선.addSection(new Section(사호선, new Station("사당역"), new Station("이수역"), 5));
        이호선.addSection(new Section(사호선, new Station("이수역"), new Station("동작역"), 5));
        이호선.addSection(new Section(사호선, new Station("동작역"), new Station("이촌역"), 10));
        이호선.addSection(new Section(사호선, new Station("이촌역"), new Station("용산역"), 10));

        final Line 중앙선 = new Line("중앙선", "bg-blue-400", new Station("강남역"), new Station("용산역"), 25);
        중앙선.addSection(new Section(중앙선, new Station("강남역"), new Station("중앙역"), 12));

        lines = Arrays.asList(이호선, 사호선, 중앙선);
    }

    @Test
    @DisplayName("최단 경로 구하기")
    void getShortestPath() {
        // given
        final Station start = new Station("강남역");
        final Station destination = new Station("용산역");
        final JgraphPath jgraphPath = new JgraphPath();
        // when
        final GraphPath graphPath = jgraphPath.getShortestPath(lines, start, destination);
        // then
        assertThat(graphPath.getVertexList()).containsExactlyElementsOf(Arrays.asList(new Station("강남역"), new Station("중앙역"), new Station("용산역")));
        assertThat(graphPath.getWeight()).isEqualTo(25);
    }
}
