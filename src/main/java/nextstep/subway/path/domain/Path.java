package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.path.policy.AgeDiscountPolicy;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> stations;

    private final List<SectionEdge> sectionEdges;

    private final int distance;


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


    public int calculateExtraFare(AuthMember authMember) {
        int calculateFare = DistanceFare.calculateDistanceFare(distance) + maxedLineExtraFare();
        AgeDiscountPolicy discountPolicy = AgeFare.getDiscountPolicy(authMember);
        return discountPolicy.discount(calculateFare);
    }

    private int maxedLineExtraFare() {
        return sectionEdges.stream()
                .mapToInt(SectionEdge::getLineExtraFare)
                .max()
                .orElse(0);
    }

}
