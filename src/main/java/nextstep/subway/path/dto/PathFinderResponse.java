package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class PathFinderResponse {

    private final List<Station> stations;
    private final int distance;
    private final int lineSurcharge;

    public PathFinderResponse(List<Station> stations, int distance, int lineSurcharge) {
        this.stations = stations;
        this.distance = distance;
        this.lineSurcharge = lineSurcharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getLineSurcharge() {
        return lineSurcharge;
    }
}
