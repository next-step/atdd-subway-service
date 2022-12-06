package nextstep.subway.path.fare.policy.extra;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public enum ExtraFarePolicyType {
    BASIC(new BasicExtraFarePolicyStrategy()),
    UNTIL_FIFTY_KILO(new UntilFiftyKiloExtraFarePolicyStrategy()),
    ABOVE_FIFTY(new AboveFiftyKiloExtraFarePolicyStrategy()),
    LINE(new LineExtraFarePolicyStrategy())
    ;

    private final ExtraFarePolicyStrategy extraFarePolicyStrategy;

    ExtraFarePolicyType(ExtraFarePolicyStrategy extraFarePolicyStrategy) {
        this.extraFarePolicyStrategy = extraFarePolicyStrategy;
    }

    public Fare calculate(Path path) {
        return extraFarePolicyStrategy.calculate(path);
    }
}
