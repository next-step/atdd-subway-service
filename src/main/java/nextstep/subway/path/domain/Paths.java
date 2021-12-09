package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paths {
    private final List<Station> stations = new ArrayList<>();
    private final Distance distance;

    public Paths(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.stations.addAll(graphPath.getVertexList());
        this.distance = Distance.of(graphPath.getWeight());
    }

    public static Paths of(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return new Paths(graphPath);
    }

    public int calculateFare() {
        return DistanceFareCalculator.calculateOverFare(distance);
    }

    public List<Station> getStations() {
        if (stations.isEmpty()) {
            return Collections.emptyList();
        }
        return stations;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
