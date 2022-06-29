package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
    private List<Section> sections;
    private List<Station> stations;
    private WeightedMultigraph<Station, SectionEdge> subwayGraph = new WeightedMultigraph<>(SectionEdge.class);

    public SubwayGraph() {
    }

    public SubwayGraph(final List<Section> sections, final List<Station> stations) {
        this.sections = sections;
        this.stations = stations;
        setVertexes();
        setEdges();
    }

    private void setVertexes() {
        stations.stream().forEach(station -> {
            subwayGraph.addVertex(station);
        });
    }

    private void setEdges() {
        sections.stream().forEach(section -> {
            final SectionEdge edge = SectionEdge.of(section);
            subwayGraph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            subwayGraph.setEdgeWeight(edge, section.getDistance());
        });
    }

    public Path findShortestPath(final Station source, final Station target) {
        if (source == target) {
            throw new RuntimeException("출발역과 도착역은 같을 수 없습니다.");
        }
        final GraphPath graphPath = getGraphPath(source, target);
        return new Path(graphPath.getVertexList(), getLinesOfGraphPah(graphPath), (int) graphPath.getWeight());
    }

    private GraphPath getGraphPath(final Station source, final Station target) {
        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        final GraphPath graphPath = dijkstraShortestPath.getPath(source, target);
        if (null == graphPath) {
            throw new RuntimeException("경로가 존재하지 않습니다.");
        }
        return graphPath;
    }

    private Set<Line> getLinesOfGraphPah(final GraphPath graphPath) {
        final List<SectionEdge> edges = graphPath.getEdgeList();
        final Set<Line> lines = new HashSet<>();
        edges.forEach(edge -> {
            lines.add(edge.getSection().getLine());
        });
        return lines;
    }
}
