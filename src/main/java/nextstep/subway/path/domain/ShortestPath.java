package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.constant.MemberFarePolicy;
import nextstep.subway.path.vo.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class ShortestPath {

    private static final int NOT_CHARGE = 0;
    private static final int PER_CHARGE_DISTANCE = 5;
    private static final int CHANGE_CHARGE_DISTANCE = 50;
    private static final int PER_CHARGE_DISTANCE_OVER = 8;
    private static final int STANDARD_DISTANCE = 10;
    private static final int STANDARD_FARE = 1250;
    private static final int OVER_CHARGE_PER_KILOMETER = 100;

    private final GraphPath<Station, SectionEdge> shortestPath;

    public ShortestPath(GraphPath<Station, SectionEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public int calculateFare(MemberFarePolicy memberFarePolicy) {
        int distance = (int) shortestPath.getWeight();
        int fare = STANDARD_FARE + calculateExtraChargeByLine() + calculateAdditionalChargeBy(distance);
        return applyFarePolicyTo(fare, memberFarePolicy);
    }

    private int calculateExtraChargeByLine() {
        List<SectionEdge> shortestRouteSectionEdges = shortestPath.getEdgeList();
        return shortestRouteSectionEdges.stream()
                .map(SectionEdge::getLine)
                .filter(line -> line.getExtraCharge() != null)
                .mapToInt(Line::getExtraCharge)
                .max()
                .orElse(NOT_CHARGE);
    }

    private int calculateAdditionalChargeBy(int distance) {
        int charge = NOT_CHARGE;
        if (distance > CHANGE_CHARGE_DISTANCE) {
            int section = distance - CHANGE_CHARGE_DISTANCE;
            charge += getCharge(section, PER_CHARGE_DISTANCE_OVER);
            distance -= section;
        }
        if (distance > STANDARD_DISTANCE) {
            int section = distance - STANDARD_DISTANCE;
            charge += getCharge(section, PER_CHARGE_DISTANCE);
        }
        return charge;
    }

    private int getCharge(int section, int perChargeDistanceOver) {
        return (int) ((Math.ceil((section - 1) / perChargeDistanceOver) + 1) * OVER_CHARGE_PER_KILOMETER);
    }

    private int applyFarePolicyTo(int fare, MemberFarePolicy memberFarePolicy) {
        if (memberFarePolicy != null) {
            return (int) Math.ceil((fare - memberFarePolicy.getDeductionAmount()) * (1 - memberFarePolicy.getDiscountPercent()));
        }
        return fare;
    }

    public List<Station> getRoutes() {
        return shortestPath.getVertexList();
    }

    public int getDistance() {
        return (int) shortestPath.getWeight();
    }
}
