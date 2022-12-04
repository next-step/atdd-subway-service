package nextstep.subway.fare.policy;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.path.domain.Path;

public class LineSurchargePolicy extends FarePolicyDecorator {
    private final Path path;

    private LineSurchargePolicy(FarePolicy farePolicy, Path path) {
        super(farePolicy);
        this.path = path;
    }

    public static LineSurchargePolicy of(FarePolicy farePolicy, Path path) {
        return new LineSurchargePolicy(farePolicy, path);
    }

    @Override
    public Fare calculateFare() {
        return super.calculateFare().add(path.maxLineSurcharge());
    }
}
