package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private List<StationResponse> stationResponses = new ArrayList<>();
    private int distance;
    private BigDecimal fare;

    public PathResponse(List<StationResponse> stationResponses, int distance, BigDecimal fare) {
        this.stationResponses = stationResponses;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse() {
    }

    public static PathResponse from(Path path, BigDecimal fare) {
        return new PathResponse(path.getStationResponses(), path.getDistance(), fare);
    }

    public List<StationResponse> getStations() {
        return stationResponses;
    }

    public int getDistance() {
        return distance;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
