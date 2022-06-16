package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResult {
    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    private PathResult(List<Station> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResult of(List<Station> stations, Distance distance, Fare fare) {
        return new PathResult(stations, distance, fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return this.distance.getDistance();
    }

    public int getFare() {
        return this.fare.getFare();
    }
}
