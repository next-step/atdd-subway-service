package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import java.util.List;

public class Path {
    private List<Station> stations;
    private int distance;
    private int fare;

    protected Path() {}

    private Path(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, int distance, int fare) {
        return new Path(stations, distance, fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public void calculate(DiscountPolicy discountPolicy, LoginMember loginMember) {
        FareType fareType = FareType.findByDistance(distance);
        int fareByDistance = fareType.calculateByDistance(distance);
        int discount = discountPolicy.calculate(fareByDistance, loginMember);
        fare += (fareByDistance - discount);
    }
}
