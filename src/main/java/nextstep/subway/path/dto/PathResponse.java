package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private final Distance distance;
    private final List<Station> stations;
    private final int fare;

    public PathResponse(Distance distance, List<Station> stations, int fare) {
        this.distance = distance;
        this.stations = stations;
        this.fare = fare;
    }

    public static PathResponse of(Path path, int fare) {
        return new PathResponse(path.getDistance(), path.getStations(), fare);
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getFare() {
        return fare;
    }

    public static PathResponse of(Distance distance, List<Station> stations, int fare) {
        return new PathResponse(distance, stations, fare);
    }
}