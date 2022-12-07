package nextstep.subway.path.ui;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse(final List<StationResponse> stations, final Distance distance, final int fare) {
        this.stations = stations;
        this.distance = distance.value();
        this.fare = fare;
    }

    protected PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public void applyDiscountFare(final int age) {
        fare = FareCalculator.applyDiscountFare(fare, age);
    }
}
