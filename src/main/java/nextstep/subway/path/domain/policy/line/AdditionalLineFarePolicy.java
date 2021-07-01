package nextstep.subway.path.domain.policy.line;

import nextstep.subway.path.domain.policy.FarePolicy;

public class AdditionalLineFarePolicy implements FarePolicy{

    private final int lineFare;

    public AdditionalLineFarePolicy(int lineFare) {
        this.lineFare = lineFare;
    }

    @Override
    public int calculate(int fare) {
        return lineFare + fare;
    }
}
