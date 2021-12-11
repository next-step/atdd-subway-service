package nextstep.subway.path.component;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

@Component
public class PathFinder {
    public PathResponse findPath(Graph<Station, DefaultWeightedEdge> graph, Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        List<Station> stations = path.getVertexList();

        List<StationResponse> responses = StationResponses.from(stations)
            .getResponses();

        int distance = (int)path.getWeight();

        return new PathResponse(responses, distance);
    }
}
