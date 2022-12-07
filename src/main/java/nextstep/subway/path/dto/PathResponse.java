package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final long amount;

    private PathResponse(List<StationResponse> stations, int distance, long amount) {
        this.stations = stations;
        this.distance = distance;
        this.amount = amount;
    }

    public static PathResponse from(Path path) {
        return new PathResponse(
            path.getStations().getList().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()),
            path.getDistanceValue(),
            path.getAmountValue()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public long getAmount() {
        return amount;
    }
}
