package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    private int fare;

    public Path(List<Station> stations, Distance distance, int maxLineFare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = FareCalculator.calculateFare(distance) + maxLineFare;
    }

    public void calculateFare(LoginMember loginMember) {
        fare = DiscountCalculator.getFare(loginMember, fare);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
