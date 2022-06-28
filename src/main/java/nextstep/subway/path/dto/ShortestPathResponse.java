package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathResponse {
    private final List<PathStation> stations = new ArrayList<>();
    private int distance;
    private int totalFare;

    public ShortestPathResponse() {}

    public ShortestPathResponse(List<Station> originalStations, int distance, int totalFare) {
        originalStations.forEach(station -> this.stations.add(PathStation.of(station)));
        this.distance = distance;
        this.totalFare = totalFare;
    }

    public List<PathStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getTotalFare() {
        return this.totalFare;
    }
}
