package nextstep.subway.path.domain;

import nextstep.subway.common.Money;
import nextstep.subway.line.domain.Line;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultLineFee implements LineFee {

    @Override
    public Money settle(final List<Line> lines) {
        return lines
                .stream()
                .map(Line::getSurcharge)
                .max(Money::compareTo)
                .orElse(Money.zero());
    }
}
