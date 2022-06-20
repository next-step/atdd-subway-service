package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
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
    private WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph;

    private Map<Long, Station> stationsCache;

    private PathFinder(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, Map<Long, Station> stationsCache) {
        this.pathGraph = pathGraph;
        this.stationsCache = stationsCache;
    }

    public static PathFinder init(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(pathGraph, stations);
        setEdgeWeight(pathGraph, sections);
        return new PathFinder(pathGraph, cacheAllStations(stations));
    }

    private static void addVertex(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Station> stations) {
        stations.forEach(it -> pathGraph.addVertex(it.getId()));
    }

    private static void setEdgeWeight(WeightedMultigraph<Long, DefaultWeightedEdge> pathGraph, List<Section> sections) {
        sections.forEach(it -> {
            Station source = it.getUpStation();
            Station target = it.getDownStation();
            int distance = it.getDistance();

            pathGraph.setEdgeWeight(pathGraph.addEdge(source.getId(), target.getId()), distance);
        });
    }

    private static Map<Long, Station> cacheAllStations(List<Station> stations) {
        return stations.stream()
                .collect(toMap(Station::getId, Function.identity()));
    }

    public Path findShortestPath(Long sourceStationId, Long targetStationId) {
        GraphPath<Long, DefaultWeightedEdge> shortestPath = findGraphPath(sourceStationId, targetStationId);
        List<Long> stationsIds = shortestPath.getVertexList();

        return Path.of(getStationsByIds(stationsIds), (int)shortestPath.getWeight());
    }

    private GraphPath<Long, DefaultWeightedEdge> findGraphPath(Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(pathGraph);
        try {
            return dijkstraShortestPath.getPath(sourceStationId, targetStationId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("출발역 혹은 도착역이 존재하지 않습니다.");
        }
    }

    private List<Station> getStationsByIds(List<Long> stationIds) {
        return stationIds.stream()
                .map(stationId -> stationsCache.get(stationId))
                .collect(Collectors.toList());
    }
}
