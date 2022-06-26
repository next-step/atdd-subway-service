package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
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

    public Fare calculate(Distance distance, List<Line> lines, int age) {
        Fare distanceFare = distanceFarePolicy.calculate(distance);
        Fare lineFare = lineFarePolicy.calculate(lines);
        Fare distanceLineFare = distanceFare.plus(lineFare);

        Fare calculate = ageFarePolicy.calculate(distanceLineFare, age);

        return calculate;
    }

}
