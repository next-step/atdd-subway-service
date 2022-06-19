package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final Integer distance;
    private Integer fare;

    public Path(List<Station> stations, Integer distance, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> vertexList, double weight, Integer lineOverFare) {
        return new Path(vertexList, (int) weight, lineOverFare);
    }

    public void calculateFare(LoginMember loginMember) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.findByDistance(distance);
        fare += distanceFarePolicy.calculate(distance);
        AgeFarePolicy ageFarePolicy = AgeFarePolicy.findByAge(loginMember.getAge());
        this.fare = ageFarePolicy.calculate(fare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
