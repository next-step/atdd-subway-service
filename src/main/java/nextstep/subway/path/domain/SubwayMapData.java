package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Objects;

public class SubwayMapData {
    private static final String NO_LINES_EXCEPTION = "경로 조회에 필요한 노선도 값이 조회되지 않습니다.";
    private static final String NO_GRAPH_EXCEPTION = "조회를 위한 그래프 값은 빈 값이 올 수 없습니다.";

    private final Lines lines;
    private final AbstractBaseGraph<Station, DefaultWeightedEdge> graph;

    public SubwayMapData(Lines lines, AbstractBaseGraph graph) {
        validateLines(lines);
        validateGraph(graph);
        this.lines = lines;
        this.graph = graph;
    }

    private void validateGraph(AbstractBaseGraph graph) {
        if (Objects.isNull(graph)) {
            throw new IllegalArgumentException(NO_GRAPH_EXCEPTION);
        }
    }

    public AbstractBaseGraph<Station, DefaultWeightedEdge> initData() {
        initVertex();
        initEdgeWeight();
        return graph;
    }

    private void initVertex() {
        lines.getStations()
                .forEach(graph::addVertex);
    }

    private void initEdgeWeight() {
        lines.getSections()
                .forEach(section -> {
                    SectionEdge sectionEdge = new SectionEdge(section);
                    graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, section.getDistance());
                });
    }

    private void validateLines(Lines lines) {
        if (Objects.isNull(lines)) {
            throw new IllegalArgumentException(NO_LINES_EXCEPTION);
        }
    }
}
