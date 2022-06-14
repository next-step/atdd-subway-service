package nextstep.subway.path.domain;

import nextstep.subway.error.ErrorCode;
import nextstep.subway.error.ErrorCodeException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathFinder {

    public PathResponse findPath(List<Station> stations, List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(graph, stations);
        setEdgeWeight(graph, lines);
        return pathResponse(graph, source, target);
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

    private PathResponse pathResponse(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = new DijkstraShortestPath<>(graph).getPath(source, target);
        if (graphPath == null) {
            throw new ErrorCodeException(ErrorCode.SOURCE_NOT_CONNECT_TARGET);
        }
        List<StationResponse> path = graphPath.getVertexList().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(path, (int) graphPath.getWeight());
    }
}
