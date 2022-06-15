package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.policy.DiscountPolicy;

public class Fare {
    private Set<Line> chargedLines;
    private int distance;
    private DiscountPolicy discountPolicy;

    public Fare(Path path, DiscountPolicy discountPolicy) {
        this.chargedLines = path.getChargedLines();
        this.distance = path.getShortestDistance();
        this.discountPolicy = discountPolicy;
    }

    public int getTotalFare() {
        return discountPolicy.discount(distanceFare() + lineExtraChargedFare());
    }

    private int lineExtraChargedFare() {
        return chargedLines.stream().
                mapToInt(line -> line.getExtraCharge()).
                max().orElse(0);
    }

    private int distanceFare() {
        return DistanceFarePolicy.calculateFare(distance);
    }
}
