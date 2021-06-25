package nextstep.subway.path.domain.line;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.EfficientLines;
import nextstep.subway.wrapped.Money;

public class LineCalculator {
    private static final DefaultLinePremiumPolicy premiumPolicy = new DefaultLinePremiumPolicy();

    public static Money calcLines(EfficientLines lines, Money money) {
        return calcFareIfSupported(premiumPolicy, lines, money);
    }

    private static Money calcFareIfSupported(DefaultLinePremiumPolicy premiumPolicy, EfficientLines lines, Money money) {
        if (premiumPolicy.isSupport(lines)) {
            money = premiumPolicy.calcFare(lines, money);
        }

        return money;
    }

}
