package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.line.domain.Section;

import java.util.List;

public class LineFare {

    private LineFare() {
    }

    public static int calculateLineFare(List<Section> sections) {
        return sections.stream()
                .map(Section::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();
    }
}
