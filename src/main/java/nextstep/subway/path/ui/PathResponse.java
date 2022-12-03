package nextstep.subway.path.ui;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int additionalFare;

    public PathResponse(final List<StationResponse> stations, final int distance, final int additionalFare) {
        this.stations = stations;
        this.distance = distance;
        this.additionalFare = additionalFare;
    }

    protected PathResponse() {}

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
            return distance;
        }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
