package nextstep.subway.path.domain;

import nextstep.subway.path.dto.Fare;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareResolver {
    private final List<FareCalculator> fareCalculators;

    public FareResolver(List<FareCalculator> fareCalculators) {
        this.fareCalculators = fareCalculators;
    }

    public Fare resolve(long distance) {
        for (FareCalculator calculator : fareCalculators) {
            if (calculator.canCalculate(distance)) {
                return calculator.calculate(distance);
            }
        }
        throw new IllegalArgumentException();
    }
}
