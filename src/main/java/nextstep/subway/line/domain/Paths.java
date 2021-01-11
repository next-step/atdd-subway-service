package nextstep.subway.line.domain;

import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.line.exceptions.SectionsException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Paths {

    private static final String NOT_FOUNT_PATHS = "찾을 수 있는 경로가 없습니다.";
    private static final String SAME_SOURCE_WITH_TARGET = "출발역과 도착역이 같습니다.";
    private static final String CANNOT_FOUND_STATION = "존재하지 않은 출발역이나 도착역입니다.";

    private final DijkstraShortestPath dijkstraShortestPath;
    private Set<Station> stations;

    public Paths(List<Section> sections) {
        this.dijkstraShortestPath = build(sections);
    }

    public PathResponse getShortestPath(Station sourceStation, Station targetStation) {
        validate(sourceStation, targetStation);
        GraphPath shortPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (shortPath == null) {
            throw new SectionsException(NOT_FOUNT_PATHS);
        }
        List<Station> shortestPath = shortPath.getVertexList();
        double shortestWeight = shortPath.getWeight();
        return PathResponse.of(shortestPath, shortestWeight);
    }

    private DijkstraShortestPath build(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.stations = getStations(sections);
        addVertex(graph);
        addEdgeAndWeight(sections, graph);
        return new DijkstraShortestPath(graph);
    }

    private void addEdgeAndWeight(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        sections.forEach(section ->
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations.forEach(graph::addVertex);
    }

    private Set<Station> getStations(List<Section> sections) {
        Set<Station> stationSet = new HashSet<>();
        sections.forEach(section -> {
            stationSet.add(section.getUpStation());
            stationSet.add(section.getDownStation());
        });
        return stationSet;
    }

    private void validate(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SectionsException(SAME_SOURCE_WITH_TARGET);
        }
        if (!stations.contains(sourceStation) || !stations.contains(targetStation)) {
            throw new SectionsException(CANNOT_FOUND_STATION);
        }
    }
}
