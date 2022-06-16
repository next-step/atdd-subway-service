package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathResponse {
    private final List<PathStation> stations = new ArrayList<>();
    private int distance;

    public ShortestPathResponse() {}

    public ShortestPathResponse(List<Station> originalStations, int distance) {
        originalStations.forEach(station -> this.stations.add(PathStation.of(station)));
        this.distance = distance;
    }

    public List<PathStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
