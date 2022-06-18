package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int charge;

    public PathResponse() {
    }

    public PathResponse(Path path) {
        this.stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = path.getDistance();
        this.charge = path.findCharge().getValue();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getCharge() {
        return charge;
    }
}
