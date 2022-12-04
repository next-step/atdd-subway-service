package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;

public class LineSurchargePolicy extends SurchargePolicyDecorator {
    private final Path path;

    private LineSurchargePolicy(SurchargePolicy surchargePolicy, Path path) {
        super(surchargePolicy);
        this.path = path;
    }

    public static LineSurchargePolicy of(SurchargePolicy surchargePolicy, Path path) {
        return new LineSurchargePolicy(surchargePolicy, path);
    }

    @Override
    public Fare calculateFare() {
        return super.calculateFare().add(path.maxLineSurcharge());
    }
}
