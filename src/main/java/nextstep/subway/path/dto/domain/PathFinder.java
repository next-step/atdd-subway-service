package nextstep.subway.path.dto.domain;

import nextstep.subway.exception.PathNotFoundException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.vo.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.utils.Message.INVALID_CONNECTED_STATIONS;
import static nextstep.subway.utils.Message.INVALID_SAME_STATIONS;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

    private PathFinder(List<Section> sections) {
        sections.forEach(it -> {
            addVertex(it.getUpStation());
            addVertex(it.getDownStation());
            graph.setEdgeWeight(graph.addEdge(it.getUpStation(), it.getDownStation()), it.getDistance());
        });
    }

    public static PathFinder from(List<Section> sections) {
        return new PathFinder(sections);
    }

    private void addVertex(Station station) {
        if (graph.containsVertex(station)) {
            return;
        }
        graph.addVertex(station);
    }

    public Path findAllStationsByStations(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = getShortestPath(source, target);

        return Path.from(graphPath);
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
