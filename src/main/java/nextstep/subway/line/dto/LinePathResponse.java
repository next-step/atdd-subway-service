package nextstep.subway.line.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class LinePathResponse {
    private List<StationResponse> stations;
    private int distance;

    public LinePathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
