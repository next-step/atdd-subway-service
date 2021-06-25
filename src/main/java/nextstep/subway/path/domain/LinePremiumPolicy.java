package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.wrapped.Money;

public interface LinePremiumPolicy {
    Money calcFare(EfficientLines lines, Money money);

    boolean isSupport(EfficientLines lines);
}
