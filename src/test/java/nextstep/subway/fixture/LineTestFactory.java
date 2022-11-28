package nextstep.subway.fixture;

import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineTestFactory {
    public static Line create(
            String name,
            String color,
            Station upStation,
            Station downStation,
            int distance,
            int extraFare
    ) {
        Line line = new Line(name, color, new ExtraFare(extraFare));
        Section section = SectionTestFactory.create(upStation, downStation, distance);
        line.addSection(section);

        return line;
    }

    private LineTestFactory() {}
}
