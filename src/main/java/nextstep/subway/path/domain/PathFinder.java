package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.NoSuchStationException;
import nextstep.subway.path.application.NotConnectedExeption;
import nextstep.subway.path.application.SameStartAndEndException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;

public class PathFinder {
    private final Set<Station> stations;
    private final List<Section> sections;
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(Set<Station> stations, List<Section> sections) {
        this.stations = stations;
        this.sections = sections;
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public PathResponse findShortestPath(Station start, Station end) {
        validate(start, end);
        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            graph.setEdgeWeight(graph.addEdge(section.getDownStation(), section.getUpStation()), section.getDistance());
        });

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(start, end);

        if(path == null) throw new NotConnectedExeption(start, end);

        return new PathResponse(path.getVertexList(), (int) path.getWeight());
    }

    private void validate(Station start, Station end) {
        if(start == null || end == null) {
            throw new NoSuchStationException("존재하지 않는 역입니다.");
        }

        if(start == end) {
            throw new SameStartAndEndException(start);
        }
    }
}
