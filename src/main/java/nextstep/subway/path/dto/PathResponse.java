package nextstep.subway.path.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    public PathResponse(Path path) {
        this.stations = path.getStations().stream().map(StationResponse::of).collect(Collectors.toList());
        this.distance = path.getDistance();
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathResponse that = (PathResponse) o;
        return that.distance == distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }

}
