package nextstep.subway.path.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class ShortestPath {
    public static final int ZERO_VALUE = 0;
    private final List<Station> stations;
    private final List<PathEdge> pathEdges;
    private final int distance;

    private ShortestPath(List<Station> stations, List<PathEdge> pathEdges,int distance) {
        this.stations = stations;
        this.pathEdges = pathEdges;
        this.distance = distance;
    }

    public static ShortestPath of(List<Station> stations, List<PathEdge> pathEdges, int distance) {
        return new ShortestPath(stations, pathEdges, distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Fare getMaxPathFare() {
        return this.pathEdges.stream()
                .map(PathEdge::getFare)
                .max(Fare::compareTo)
                .orElse(Fare.of(ZERO_VALUE));
    }

    public int getDistance() {
        return distance;
    }
}
