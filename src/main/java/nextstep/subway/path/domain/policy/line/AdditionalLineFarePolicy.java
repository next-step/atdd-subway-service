package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class AdditionalLineFarePolicy implements FarePolicy{

    @Override
    public int calculate(int fare) {
        return 0;
    }
}
