package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.amount.domain.Amount;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Stations;
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

    public static PathResponse of(Stations stations, Distance distance, Amount amount) {
        return new PathResponse(
            stations.getList().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()),
            distance.value(),
            amount.value()
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
