package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final BigDecimal fare;

    private PathResponse(List<Station> stations, Distance distance, Fare fare) {
        this.stations = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        this.distance = distance.value();
        this.fare = fare.value();
    }

    public static PathResponse of(Path path, Fare fare) {
        return new PathResponse(path.getStations(), path.getDistance(), fare);
    }

    public List<StationResponse> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
