package nextstep.subway.path.ui.dto;

import nextstep.subway.path.domain.SafeStationInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationInPathResponse> stations;
    private final double distance;
    private final BigDecimal fee;

    public PathResponse(List<StationInPathResponse> stations, double distance, BigDecimal fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static PathResponse of(List<SafeStationInfo> safeStationInfos, double distance, BigDecimal fee) {
        List<StationInPathResponse> stations = safeStationInfos.stream()
                .map(StationInPathResponse::new)
                .collect(Collectors.toList());

        return new PathResponse(stations, distance, fee);
    }

    public List<StationInPathResponse> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public BigDecimal getFee() {
        return fee;
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
