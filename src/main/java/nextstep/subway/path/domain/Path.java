package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Path {

    private final List<Station> stations;
    private final double distance;

    private Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path from(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        return new Path(shortestPath.getVertexList(), shortestPath.getWeight());
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }
}
