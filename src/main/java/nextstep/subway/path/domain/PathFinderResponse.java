package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public final class PathFinderResponse {
    private final List<Station> stations;
    private final int distance;

    public PathFinderResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
