package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResult {

    private final List<Station> stations;
    private final int distance;
    private final Fare fare;

    public PathResult(List<Station> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResult(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = new Fare(fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getValue();
    }
}
