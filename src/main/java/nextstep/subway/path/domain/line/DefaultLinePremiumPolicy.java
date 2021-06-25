package nextstep.subway.path.domain.line;

import nextstep.subway.path.domain.EfficientLines;
import nextstep.subway.path.domain.LinePremiumPolicy;
import nextstep.subway.wrapped.Money;

public class DefaultLinePremiumPolicy implements LinePremiumPolicy {
    @Override
    public Money calcFare(EfficientLines lines, Money money) {
        return money.plus(lines.findExpensiveFare());
    }

    @Override
    public boolean isSupport(EfficientLines lines) {
        return true;
    }
}
