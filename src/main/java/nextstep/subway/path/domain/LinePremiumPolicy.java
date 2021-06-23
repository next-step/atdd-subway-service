package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

public interface LinePremiumPolicy {
    Money calcFare(Lines lines, Money money);
}
