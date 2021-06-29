package nextstep.subway.path.domain.line;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Calculator;
import nextstep.subway.path.domain.EfficientLines;
import nextstep.subway.path.domain.ShortestDistance;
import nextstep.subway.wrapped.Money;

public class LineCalculator implements Calculator {
    private static final DefaultLinePremiumPolicy premiumPolicy = new DefaultLinePremiumPolicy();

    @Override
    public Money calc(Money money, LoginMember loginMember, ShortestDistance shortestDistance) {
        return calcFareIfSupported(premiumPolicy, new EfficientLines(shortestDistance.usedLines()), money);
    }

    private Money calcFareIfSupported(DefaultLinePremiumPolicy premiumPolicy, EfficientLines lines, Money money) {
        if (premiumPolicy.isSupport(lines)) {
            money = premiumPolicy.calcFare(lines, money);
        }

        return money;
    }
}
