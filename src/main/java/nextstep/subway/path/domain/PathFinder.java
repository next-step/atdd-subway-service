package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionNotConnectedException;
import nextstep.subway.exception.StationsNotExistException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.wrapper.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class PathFinder {
    private Map<Long, Station> allStations;
    private WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph;

    private PathFinder(final Map<Long, Station> allStations, final WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph) {
        this.allStations = allStations;
        this.pathGraph = pathGraph;
    }

    public static PathFinder init(final List<Station> allStations, final List<Section> allSections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(pathGraph, allStations);
        setEdgesWeight(pathGraph, allSections);
        return new PathFinder(allStationsToMap(allStations), pathGraph);
    }

    private static void addVertex(final WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, final List<Station> allStations) {
        for (Station station : allStations) {
            pathGraph.addVertex(station.getId());
        }
    }

    private static void setEdgesWeight(final WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, final List<Section> allSections) {
        for (Section section : allSections) {
            Station sourceStation = section.getUpStation();
            Station targetStation = section.getDownStation();
            Distance sectionDistance = section.getDistance();
            pathGraph.setEdgeWeight(pathGraph.addEdge(sourceStation.getId(), targetStation.getId()), sectionDistance.getDistance());
        }
    }

    private static Map<Long, Station> allStationsToMap(final List<Station> allStations) {
        return allStations.stream()
                .collect(toMap(Station::getId, Function.identity()));
    }

    public Path findShortestPath(final Long sourceStationId, final Long targetStationId) {
        GraphPath<Long, DefaultWeightedEdge> shortestPath = findPathGraph(sourceStationId, targetStationId);
        throwIfNotConnectedSection(shortestPath);
        return new Path(getShortestPathStations(shortestPath.getVertexList()), new Double(shortestPath.getWeight()).intValue());
    }

    private GraphPath<Long, DefaultWeightedEdge> findPathGraph(final Long sourceStationId, final Long targetStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPathFinder = new DijkstraShortestPath<>(pathGraph);
        try {
            return shortestPathFinder.getPath(sourceStationId, targetStationId);
        } catch (IllegalArgumentException e) {
            throw new StationsNotExistException();
        }
    }

    private void throwIfNotConnectedSection(final GraphPath<Long, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new SectionNotConnectedException();
        }
    }

    private List<Station> getShortestPathStations(final List<Long> shortestPathStationIds) {
        return shortestPathStationIds.stream()
                .map(stationId -> allStations.get(stationId))
                .collect(Collectors.toList());
    }
}
