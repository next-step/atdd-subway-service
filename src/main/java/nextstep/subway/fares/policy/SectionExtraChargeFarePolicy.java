package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Path;

public class SectionExtraChargeFarePolicy implements FarePolicy {
    @Override
    public void calculateFare(Fare fare, Path path) {
        fare.add(path.getMaxExtraCharge());
    }
}
