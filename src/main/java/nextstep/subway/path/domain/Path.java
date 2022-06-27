package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.fare.domain.AgeType;
import nextstep.subway.fare.domain.DistanceType;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.policy.DiscountPolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private List<Line> lines;
    private List<Station> stations;
    private int distance;

    private Path(List<Line> lines, List<Station> stations, int distance) {
        this.lines = lines;
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Line> lines, List<Station> stations, int distance) {
        return new Path(lines, stations, distance);
    }

    public Fare calculateFare(AuthMember authMember) {
        int calculateFare = DistanceType.distanceExtraFare(this.distance) + findLineExtraFare();
        DiscountPolicy discountPolicy = AgeType.getDiscountPolicy(authMember);
        return Fare.from(discountPolicy.discount(calculateFare));
    }

    private int findLineExtraFare() {
        return this.lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
