package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.path.exception.SameOriginAndDestinationException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(Lines lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexs(lines.getStations());
        addEdges(lines.getSections());
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addVertexs(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void addEdges(List<Section> sections) {
        sections.forEach(this::setEdgeWeight);
    }

    private void setEdgeWeight(Section section) {
        graph.setEdgeWeight(addEdge(section), section.getDistanceWeight());
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public PathResponse findPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new SameOriginAndDestinationException();
        }

        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        if (graphPath == null) {
            throw new PathNotFoundException();
        }
        List<Station> stations = graphPath.getVertexList();
        return new PathResponse(StationResponse.ofList(stations), (int) graphPath.getWeight());
    }
}
