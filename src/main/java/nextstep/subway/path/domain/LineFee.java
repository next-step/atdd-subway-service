package nextstep.subway.path.domain;

import nextstep.subway.common.Money;

import java.util.List;

public interface LineFee {

    Money settle(final List<Long> lineIds);
}
