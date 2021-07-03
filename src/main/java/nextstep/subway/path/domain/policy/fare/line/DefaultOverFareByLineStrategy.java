package nextstep.subway.path.domain.policy.fare.line;

import nextstep.subway.line.domain.Line;

import java.util.Comparator;
import java.util.List;

public class DefaultOverFareByLineStrategy implements OverFareByLineStrategy {
    @Override
    public int calculateOverFare(List<Line> lines) {
        final Comparator<Line> comp = (o1, o2) -> Integer.compare(o1.getOverFare().value(), o2.getOverFare().value());
        return lines.stream()
                .sorted(comp.reversed())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException())
                .getOverFare()
                .value();
    }
}
