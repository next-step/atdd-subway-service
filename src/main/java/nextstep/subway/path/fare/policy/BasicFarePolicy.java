package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import org.springframework.stereotype.Component;

@Component
public class BasicFarePolicy implements FarePolicy {

    public static final Distance MAX_DISTANCE = Distance.valueOf(10);
    public static final Fare BASIC_FARE = Fare.valueOf(1_250);

    @Override
    public Fare calculate(Path path) {
        return BASIC_FARE;
    }
}
