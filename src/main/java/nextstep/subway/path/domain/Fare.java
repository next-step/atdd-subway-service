package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.constant.MemberFarePolicy;
import nextstep.subway.path.vo.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Fare {

    private static final int NOT_CHARGE = 0;
    private static final int PER_CHARGE_DISTANCE = 5;
    private static final int CHANGE_CHARGE_DISTANCE = 50;
    private static final int PER_CHARGE_DISTANCE_OVER = 8;
    private static final int STANDARD_DISTANCE = 10;
    private static final int STANDARD_FARE = 1250;

    private final GraphPath<Station, SectionEdge> shortestPath;
    private final MemberFarePolicy memberFarePolicy;

    public Fare(GraphPath<Station, SectionEdge> shortestPath, MemberFarePolicy memberFarePolicy) {
        this.shortestPath = shortestPath;
        this.memberFarePolicy = memberFarePolicy;
    }

    public int calcFare() {
        int distance = (int) shortestPath.getWeight();
        int fare = STANDARD_FARE + calcExtraChargeByLine() + calcAdditionalChargeBy(distance) ;
        return applyFarePolicyTo(fare);
    }

    private int calcExtraChargeByLine() {
        List<SectionEdge> shortestRouteSectionEdges = shortestPath.getEdgeList();
        return shortestRouteSectionEdges.stream()
                .map(SectionEdge::getLine)
                .filter(line -> line.getExtraCharge() != null)
                .mapToInt(Line::getExtraCharge)
                .max()
                .orElse(NOT_CHARGE);
    }

    private int calcAdditionalChargeBy(int distance) {
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
        return (int) ((Math.ceil((section - 1) / perChargeDistanceOver) + 1) * 100);
    }

    private int applyFarePolicyTo(int fare) {
        if (memberFarePolicy != null) {
            return (int) Math.ceil((fare - memberFarePolicy.getDeductionAmount()) * (1 - memberFarePolicy.getDiscountPercent()));
        }
        return fare;
    }
}
