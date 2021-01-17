package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;

public class SectionExtraChargeFarePolicy implements FarePolicy {
    @Override
    public void calculateFare(Fare fare, FareContext fareContext) {
        fare.add(fareContext.getPath().getMaxExtraCharge());
    }
}
