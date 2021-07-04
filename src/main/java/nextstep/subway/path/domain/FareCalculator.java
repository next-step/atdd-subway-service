package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.IncompleteLoginMember;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class FareCalculator {

    public static FareCalculator newInstance() {
        return new FareCalculator();
    }

    public int calculateFare(GraphPath<Station, SectionEdge> path, IncompleteLoginMember incompleteLoginMember) {
        int distance = (int) path.getWeight();
        int fare = DistanceFare.findDistanceFareByDistance(distance)
                .calculateFare(distance);
        int extraFare = findMaxChargeFromLines(path);
        if (incompleteLoginMember.isCompleteLoginMember()) {
            return AgeDiscount.findAgeDiscountByAge(incompleteLoginMember.toCompleteLoginMember().getAge())
                    .discountFare(fare + extraFare);
        }
        return fare + extraFare;
    }

    private int findMaxChargeFromLines(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList().stream()
                .map(SectionEdge::getCharge)
                .max(Integer::compareTo)
                .orElseThrow(IllegalAccessError::new);
    }
}
