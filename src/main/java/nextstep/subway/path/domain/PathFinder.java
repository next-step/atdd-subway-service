package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    public PathFinder() {
    }

    public PathResponse findShortestPath(WeightedMultigraph<Station, DefaultWeightedEdge> map,
                                         Station source, Station target) {
        confirmSelectSameStation(source, target);
        confirmStationIsOnLine(map, source, target);

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(map);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        confirmNonShortestPath(path);

        List<Station> stations = path.getVertexList();
        double distance = shortestPath.getPathWeight(source, target);
        return new PathResponse(stations, distance);
    }

    public void confirmSelectSameStation(Station source, Station target) {
        if (source.getId().equals(target.getId())) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }

    public void confirmNonShortestPath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
    }

    public void confirmStationIsOnLine(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                       Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new IllegalArgumentException("존재하지 않은 지하철역을 선택했습니다.");
        }
    }
}
