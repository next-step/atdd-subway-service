package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private Distance distance;

    public PathResponse(){

    }

    public PathResponse(List<StationResponse> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }


}
