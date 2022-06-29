package nextstep.subway.fare.application;

import nextstep.subway.fare.domain.discount.DiscountPolicy;
import nextstep.subway.fare.domain.fare.OverFarePolicy;
import nextstep.subway.member.domain.AgeGrade;
import nextstep.subway.path.domain.ShortestPath;
import org.springframework.stereotype.Service;

@Service
public class FareService {
    private static final long DEFAULT_FARE = 1250;
    private final DiscountPolicy discountPolicy;
    private final OverFarePolicy overFarePolicy;

    public FareService(DiscountPolicy discountPolicy, OverFarePolicy overFarePolicy) {
        this.discountPolicy = discountPolicy;
        this.overFarePolicy = overFarePolicy;
    }

    public long calculateFare(AgeGrade ageGrade, ShortestPath shortestPath) {
        long overFare = overFarePolicy.calculateOverFare(shortestPath.getSectionEdges(), shortestPath.getDistance());
        long fare = DEFAULT_FARE + overFare;

        return discountPolicy.calculateDiscountFare(ageGrade, fare);
    }
}
