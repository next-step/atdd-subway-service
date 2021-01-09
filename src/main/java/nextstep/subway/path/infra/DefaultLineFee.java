package nextstep.subway.path.infra;

import nextstep.subway.common.Money;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.LineFee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultLineFee implements LineFee {

    private final LineRepository lineRepository;

    public DefaultLineFee(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public Money settle(final List<Long> lineIds) {
        return findAllLines(lineIds)
                .stream()
                .map(Line::getSurcharge)
                .max(Money::compareTo)
                .orElse(Money.zero());
    }

    private List<Line> findAllLines(final List<Long> lineIds) {
        return lineRepository.findAllByIds(lineIds);
    }
}
