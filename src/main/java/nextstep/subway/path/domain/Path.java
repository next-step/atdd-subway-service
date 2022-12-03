package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.AgeFarePolicy;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    private Path(List<Station> stations, Distance distance, Fare fare) {
        validatePathStations(stations);
        validatePathDistance(distance);
        this.stations = new ArrayList<>(stations);
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, Distance distance, Fare fare) {
        return new Path(stations, distance, fare);
    }

    private void validatePathStations(List<Station> stations) {
        if(stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.경로는_비어있을_수_없음.getErrorMessage());
        }
    }

    private void validatePathDistance(Distance distance) {
        if(distance.isZero()) {
            throw new IllegalArgumentException(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
        }
    }

    public void convertFareByAgeFarePolicy(AgeFarePolicy ageFarePolicy) {
        this.fare = ageFarePolicy.calculateFare(this.fare);
    }

    public List<Station> unmodifiableStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }
}
