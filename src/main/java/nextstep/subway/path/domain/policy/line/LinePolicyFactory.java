package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class LinePolicyFactory {
    public static final int BASIC_LINE_FARE = 0;

    public static FarePolicy findPolicy(int fare) {
        if (fare > BASIC_LINE_FARE) {
            return new AdditionalLineFarePolicy(fare);
        }
        return new DefaultLineFarePolicy();
    }
}
