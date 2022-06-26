package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class LineSurchargePolicy {
    private static final int ZERO = 0;

    public int fare(int fare, List<Line> lines) {
        return fare + getMaxSurcharge(lines);
    }

    private int getMaxSurcharge(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(ZERO);
    }
}
