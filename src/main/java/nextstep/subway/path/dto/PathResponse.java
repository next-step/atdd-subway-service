package nextstep.subway.path.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final BigDecimal fare;

    public PathResponse(
        final List<StationResponse> stations,
        final int distance,
        final BigDecimal fare
    ) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(final Path path, final BigDecimal fare) {
        final List<StationResponse> stationResponses = path.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        return new PathResponse(stationResponses, path.getDistance(), fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
