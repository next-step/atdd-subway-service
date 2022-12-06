package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.util.List;

public class Path {

    List<Station> stations;
    Distance distance;

    public Path(Graph graph, Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph.getGraph());
        stations = dijkstraShortestPath.getPath(source, target).getVertexList();
        distance = new Distance((int) dijkstraShortestPath.getPath(source, target).getWeight());
    }

    public List<Station> getBestPath() {
        return stations;
    }

    public Distance getBestPathDistance() {
        return distance;
    }
}
