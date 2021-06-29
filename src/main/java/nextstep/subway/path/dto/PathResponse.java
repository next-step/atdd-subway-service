package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Paths;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private int distance;
    private List<Station> stations;

    public PathResponse() {
        // empty
    }

    public PathResponse(final Paths paths) {
        this.distance = paths.getTotalDistance();
        this.stations = paths.getShortestStationRoutes();
    }

    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
