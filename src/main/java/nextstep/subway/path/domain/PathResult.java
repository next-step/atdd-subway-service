package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;

public class PathResult {

    private List<Station> stations;
    private int distance;
    private int fare;

    private PathResult(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResult of(List<Station> stations, double distance, Fare fare) {
        return new PathResult(stations, (int) distance, fare.getFare());
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
