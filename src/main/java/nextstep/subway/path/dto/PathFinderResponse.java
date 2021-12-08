package nextstep.subway.path.dto;

import java.util.List;

import nextstep.subway.station.domain.Station;

public class PathFinderResponse {

    private final List<Station> stations;
    private final int distance;
    private final int maxLineSurcharge;

    public PathFinderResponse(List<Station> stations, int distance, int maxLineSurcharge) {
        this.stations = stations;
        this.distance = distance;
        this.maxLineSurcharge = maxLineSurcharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getMaxLineSurcharge() {
        return maxLineSurcharge;
    }
}
