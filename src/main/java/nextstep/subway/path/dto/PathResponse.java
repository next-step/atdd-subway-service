package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fee;

    public PathResponse() {
    }

    public PathResponse(Path path) {
        List<Station> stations = path.getStations();
        this.stations = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = path.getDistance();
        this.fee = path.getFee();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

}
