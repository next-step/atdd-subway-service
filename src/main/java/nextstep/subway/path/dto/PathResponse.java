package nextstep.subway.path.dto;

import nextstep.subway.line.domain.section.Money;
import nextstep.subway.station.domain.Station;

import java.math.BigDecimal;
import java.util.List;

public class PathResponse {

    private List<Station> stations;
    private int distance;
    private Money pareMoney;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance, Money pareMoney) {
        this.stations = stations;
        this.distance = distance;
        this.pareMoney = pareMoney;
    }

    public static PathResponse of(List<Station> stations, int distance, Money pareMoney) {
        return new PathResponse(stations, distance, pareMoney);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public BigDecimal getPareMoney() {
        return pareMoney.getMoney();
    }
}
