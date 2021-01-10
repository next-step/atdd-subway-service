package nextstep.subway.path.domain;

import nextstep.subway.common.Money;

public interface DistanceFee {

    Money settle(final Distance distance);
}
