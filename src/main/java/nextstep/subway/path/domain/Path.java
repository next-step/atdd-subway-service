package nextstep.subway.path.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class Path {
    private List<StationResponse> stations;
    private Distance distance;
    private Fare fare;

    protected Path() {
    }

    protected Path(List<StationResponse> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<StationResponse> stations, Distance distance, Fare fare) {
        return new Path(stations, distance, fare);
    }

    public void discountFareByAge(Integer age) {
        this.fare = fare.discountByAge(age);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public int getDistanceValue() {
        return distance.getDistance();
    }

    public Fare getFare() {
        return fare;
    }

    public int getFareValue() {
        return fare.getValue();
    }
}
