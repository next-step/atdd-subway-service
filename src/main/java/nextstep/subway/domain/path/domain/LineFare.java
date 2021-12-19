package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;

import java.util.List;

public class LineFare {

    private LineFare() {
    }

    public static int calculateLineFare(List<Line> lines) {
        if (lines.isEmpty()) {
            return 0;
        }
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();
    }

}
