package nextstep.subway.path.domain;

import nextstep.subway.common.Money;
import nextstep.subway.line.domain.Line;

import java.util.List;

public interface LineFee {

    Money settle(final List<Line> lines);
}
