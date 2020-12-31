package nextstep.subway.path.ui.dto;

import nextstep.subway.path.domain.SafeStationInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationInPathResponse> stations;
    private final double distance;

    public PathResponse(List<StationInPathResponse> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(List<SafeStationInfo> safeStationInfos, double distance) {
        List<StationInPathResponse> stations = safeStationInfos.stream()
                .map(StationInPathResponse::new)
                .collect(Collectors.toList());

        return new PathResponse(stations, distance);
    }

    public List<StationInPathResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PathResponse that = (PathResponse) o;
        return Double.compare(that.distance, distance) == 0 && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
