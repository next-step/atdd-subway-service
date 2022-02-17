package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class PathResult {

    private List<Station> stations;
    private int distance;

    private PathResult(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = (int) distance;
    }

    public static PathResult of(DijkstraShortestPath dijkstraShortestPath, Station start, Station destination) {
        return new PathResult(dijkstraShortestPath.getPath(start, destination).getVertexList(),
            dijkstraShortestPath.getPathWeight(start, destination));
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
