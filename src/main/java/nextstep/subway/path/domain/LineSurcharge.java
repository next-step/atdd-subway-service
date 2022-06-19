package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.Collection;

public class LineSurcharge extends Charge {
    private LineSurcharge(int value) {
        super(value);
    }

    public static LineSurcharge from(Collection<Line> lines) {
        return new LineSurcharge(maxSurcharge(lines));
    }

    private static int maxSurcharge(Collection<Line> lines) {
        return lines.stream()
                .map(Line::getSurcharge)
                .reduce(Math::max)
                .orElse(0);
    }
}
