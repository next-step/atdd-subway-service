package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.path.policy.AgeDiscountPolicy;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> stations;

    private final List<SectionEdge> sectionEdges;

    private final int distance;

    private static final int LINE_FARE_FREE = 0;


    public Path(List<Station> stations, List<SectionEdge> sectionEdges, int distance) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }


    public Fare calculateExtraFare(AuthMember authMember) {
        AgeDiscountPolicy discountPolicy = AgeFare.getDiscountPolicy(authMember);
        Fare calculateFare = DistanceFare.calculateDistanceFare(distance)
                .plus(maxedLineExtraFare())
                .ageDiscount(discountPolicy);
        return calculateFare;
    }

    private int maxedLineExtraFare() {
        return sectionEdges.stream()
                .mapToInt(SectionEdge::getLineExtraFare)
                .max()
                .orElse(LINE_FARE_FREE);
    }

}
