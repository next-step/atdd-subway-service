package nextstep.subway.path.domain.line;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.LinePremiumPolicy;
import nextstep.subway.wrapped.Money;

public class DefaultLinePremiumPolicy implements LinePremiumPolicy {
    @Override
    public Money calcFare(Lines lines, Money money) {
        return money.plus(lines.findExpensiveFare());
    }

    @Override
    public boolean isSupport(Lines lines) {
        return true;
    }
}
