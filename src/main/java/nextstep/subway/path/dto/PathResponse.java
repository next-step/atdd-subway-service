package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;

import java.math.BigDecimal;
import java.util.List;

public class PathResponse {
    private List<Station> stations;
    private int distance;
    private BigDecimal paymentFare;

    protected PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public PathResponse(List<Station> stations, int distance, BigDecimal paymentFare) {
        this.stations = stations;
        this.distance = distance;
        this.paymentFare = paymentFare;
    }

    public BigDecimal getPaymentFare() {
        return paymentFare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
