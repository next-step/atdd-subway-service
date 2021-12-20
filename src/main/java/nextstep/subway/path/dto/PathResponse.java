package nextstep.subway.path.dto;

import nextstep.subway.map.domain.SubwayFareCalculator;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = SubwayFareCalculator.calculate(distance);
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

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return distance == that.distance &&
                fare == that.fare &&
                Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, fare);
    }
}
