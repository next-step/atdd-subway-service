package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<StationResponse> stations, int distance) {
        return new PathResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return distance == that.distance &&
                Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
