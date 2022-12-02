package nextstep.subway.path.domain;

import nextstep.subway.exception.PathNotFoundException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.utils.Message.*;

public class DijkstraPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    private DijkstraPathFinder(List<Section> sections) {
        sections.forEach(it -> {
            addVertex(it.getUpStation());
            addVertex(it.getDownStation());
            graph.setEdgeWeight(graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance());
        });
    }

    public static DijkstraPathFinder from(List<Section> sections) {
        return new DijkstraPathFinder(sections);
    }

    private void addVertex(Station station) {
        if (graph.containsVertex(station)) {
            return;
        }
        graph.addVertex(station);
    }

    @Override
    public List<Station> findAllStationsByStations(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestPath(source, target);
        return path.getVertexList();
    }

    @Override
    public int findShortestDistance(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = getShortestPath(source, target);
        return (int) path.getWeight();
    }

    private GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station source, Station target) {
        checkSameStations(source, target);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        checkConnectedPath(path);
        return path;
    }


    private void checkSameStations(Station source, Station target) {
        if(source.equals(target)) {
            throw new PathNotFoundException(INVALID_SAME_STATIONS);
        }
    }

    private void checkConnectedPath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new PathNotFoundException(INVALID_CONNECTED_STATIONS);
        }
    }
}
