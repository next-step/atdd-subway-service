package nextstep.subway.policy.price;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import nextstep.subway.line.domain.Line;
import nextstep.subway.policy.domain.Price;

public class LinePricePolicy implements PricePolicy {
    List<Line> lines;

    public LinePricePolicy(List<Line> lines) {
        super();

        this.lines = lines;
    }

    @Override
    public Price apply() {
        return lines.stream()
                    .map(line -> line.getExtreFare())
                    .max(Comparator.comparing(Price::value))
                    .orElseThrow(() -> new NoSuchElementException("라인 가격정책에 계산되는 값이 없습니다."));
    }
}
