package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareCalculator {
    private final DistanceFarePolicy distanceFarePolicy;
    private final LineFarePolicy lineFarePolicy;
    private final AgeFarePolicy ageFarePolicy;

    public FareCalculator(DistanceFarePolicy distanceFarePolicy, LineFarePolicy lineFarePolicy, AgeFarePolicy ageFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.lineFarePolicy = lineFarePolicy;
        this.ageFarePolicy = ageFarePolicy;
    }

    public Fare calculate(List<SectionEdge> sectionEdges, Distance distance) {
        Fare distanceExtraFare = distanceFarePolicy.calculate(distance);
        Fare lineExtraFare = lineFarePolicy.calculate(sectionEdges);
        return distanceExtraFare.plus(lineExtraFare);
    }

    public Fare discountByAge(Fare fare, int age) {
        return ageFarePolicy.discount(fare, age);
    }
}
