package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public class PathResponse {

    private List<Station> stations;
    private int distance;
    private long fare;

    public PathResponse() {
    }

    public PathResponse(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(final List<Station> stations, final int distance, final long fare) {
        this(stations, distance);
        this.fare = fare;
    }

    public static PathResponse of(final Path path) {
        return new PathResponse(path.getStations(), path.getDistance());
    }

    public static PathResponse of(final PathResponse pathResponse, final long fare) {
        return new PathResponse(pathResponse.getStations(), pathResponse.getDistance(), fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
