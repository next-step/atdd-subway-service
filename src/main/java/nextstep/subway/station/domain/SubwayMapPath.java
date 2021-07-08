package nextstep.subway.station.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.SectionEdge;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayMapPath {

    private static final int BASIC_FARE = 1_250;

    private GraphPath graphPath;

    public SubwayMapPath(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> stations() {
        return graphPath.getVertexList();
    }

    public int distance() {
        return (int)graphPath.getWeight();
    }

    public int fare(LoginMember loginMember) {
        int fare = fare();
        return fare - discountFare(loginMember, fare);
    }

    private int discountFare(LoginMember loginMember, int fare) {
        if (loginMember.isEmpty()) {
            return AgeDiscountPolicy.DISCOUNT_NONE;
        }
        return AgeDiscountPolicy.discountFare(loginMember.getAge(), fare);
    }

    private int fare() {
        return BASIC_FARE + extraFare() + DistanceBasedFarePolicy.overFare(distance());
    }

    private int extraFare() {
        List<Integer> extraFares = sections().stream()
                                            .map(SectionEdge::getExtraFare)
                                            .collect(Collectors.toList());
        return Collections.max(extraFares);
    }

    private List<SectionEdge> sections() {
        return graphPath.getEdgeList();
    }
}
