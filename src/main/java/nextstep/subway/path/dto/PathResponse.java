package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public class PathResponse {

    private List<Station> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(path.getStations(), path.getDistance().getDistance());
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }
}
