package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.math.BigDecimal;
import java.util.List;

public class PathResponse {

    private List<Station> stations;
    private int distance;
    private BigDecimal fareMoney;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance, BigDecimal fareMoney) {
        this.stations = stations;
        this.distance = distance;
        this.fareMoney = fareMoney;
    }

    public static PathResponse of(List<Station> stations, int distance, BigDecimal pareMoney) {
        return new PathResponse(stations, distance, pareMoney);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public BigDecimal getFareMoney() {
        return fareMoney;
    }
}
