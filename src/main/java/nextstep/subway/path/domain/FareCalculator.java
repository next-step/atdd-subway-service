package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareCalculator {
    private final DistanceFarePolicy distanceFarePolicy;
    private final LineFarePolicy lineFarePolicy;

    public FareCalculator(DistanceFarePolicy distanceFarePolicy, LineFarePolicy lineFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.lineFarePolicy = lineFarePolicy;
    }

    public Fare calculate(LoginMember loginMember, List<SectionEdge> sectionEdges, Distance distance) {
        Fare distanceExtraFare = distanceFarePolicy.calculate(distance);
        Fare lineExtraFare = lineFarePolicy.calculate(sectionEdges);
        Fare totalFare = distanceExtraFare.plus(lineExtraFare);

        if (loginMember.isLogin()) {
            totalFare = discountByAge(totalFare, loginMember.getAge());
        }
        return totalFare;
    }

    public Fare discountByAge(Fare fare, int age) {
        return AgeFarePolicy.of(age).discount(fare);
    }
}
