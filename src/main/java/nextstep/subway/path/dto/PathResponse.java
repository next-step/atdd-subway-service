package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int totalCharge;

    public PathResponse() {

    }

    public PathResponse(final Path path, final int charge) {
        this.stations = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = path.getDistance();
        this.totalCharge = charge + path.getSurcharge();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTotalCharge() {
        return totalCharge;
    }
}
