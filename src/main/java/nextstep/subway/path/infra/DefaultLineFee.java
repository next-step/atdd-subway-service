package nextstep.subway.path.infra;

import nextstep.subway.common.Money;
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
        return lineIds.stream()
                .map(this::toLineSurcharge)
                .max(Money::compareTo)
                .orElse(Money.zero());
    }

    private Money toLineSurcharge(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalStateException(String.format("%d에 해당하는 지하철 노선이 없습니다.", lineId)))
                .getSurcharge();
    }
}
