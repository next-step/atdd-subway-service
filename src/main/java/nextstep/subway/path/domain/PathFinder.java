package nextstep.subway.path.domain;

import nextstep.subway.error.ErrorCode;
import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {

    public Path findPath(List<Station> stations, List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(graph, stations);
        setEdgeWeight(graph, lines);
        GraphPath<Station, DefaultWeightedEdge> graphPath = find(graph, source, target);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), lines);
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.forEach(line -> setEdgeWeightSections(graph, line.getSections().getSections()));
    }

    private void setEdgeWeightSections(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }

    private GraphPath<Station, DefaultWeightedEdge> find(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = new DijkstraShortestPath<>(graph).getPath(source, target);
        if (graphPath == null) {
            throw new ErrorCodeException(ErrorCode.SOURCE_NOT_CONNECT_TARGET);
        }
        return graphPath;
    }
}
