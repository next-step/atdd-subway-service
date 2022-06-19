package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public PathResponse find(List<Line> lines, Station sourceStation, Station targetStation) {
        init(lines);
        GraphPath<Station, DefaultWeightedEdge> path = findPath(sourceStation, targetStation);

        return new PathResponse(path.getVertexList(), path.getWeight());
    }

    private void init(List<Line> lines) {
        lines.stream()
                .map(Line::getSections)
                .map(Sections::getSections)
                .flatMap(Collection::stream)
                .forEach(this::setVertexAndEdgeWeight);
    }

    private void setVertexAndEdgeWeight(Section it) {
        graph.addVertex(it.getUpStation());
        graph.addVertex(it.getDownStation());
        graph.setEdgeWeight(graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance());
    }

    private GraphPath<Station, DefaultWeightedEdge> findPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

}
