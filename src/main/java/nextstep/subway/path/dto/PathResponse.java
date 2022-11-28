package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;

    private PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

}
