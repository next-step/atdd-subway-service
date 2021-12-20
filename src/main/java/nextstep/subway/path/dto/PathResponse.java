package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private final Distance distance;
    private final List<Station> stations;

    public PathResponse(Distance distance, List<Station> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(path.getDistance(), path.getStations());
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public static PathResponse of(Distance distance, List<Station> stations) {
        return new PathResponse(distance, stations);
    }
}