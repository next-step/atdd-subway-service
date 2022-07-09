package nextstep.subway.charge.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;

public class ChargeCalculator {
    private static final int DEFAULT_CHARGE = 1250;

    private final AgePolicy agePolicy;
    private final DistancePolicy distancePolicy;
    private final LinePolicy linePolicy;

    public ChargeCalculator(Integer age, Integer totalDistance, List<Section> allSections) {
        this.agePolicy = new AgePolicy(age);
        this.distancePolicy = new DistancePolicy(totalDistance);
        this.linePolicy = new LinePolicy(allSections);
    }

    public Charge calculate() {
        Charge charge = new Charge(DEFAULT_CHARGE);

        linePolicy.applyPolicy(charge);
        distancePolicy.applyPolicy(charge);
        agePolicy.applyPolicy(charge);

        return charge;
    }
}
