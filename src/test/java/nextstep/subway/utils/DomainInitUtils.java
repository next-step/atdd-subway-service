package nextstep.subway.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import java.util.Arrays;

public final class DomainInitUtils {
    private DomainInitUtils() { }

    public static PathFinder initPathFinder(Line...line) {
        Lines lines = new Lines(Arrays.asList(line));
        return new PathFinder(lines);
    }

    public static Section createSection(final Station upStation, final Station downStation, final int distance) {
        return Section.builder().id(nextstep.subway.utils.TestUtils.getRandomId())
                .upStation(upStation).downStation(downStation)
                .distance(distance)
                .build();
    }

    public static Station createStation(final String name) {
        return new Station(nextstep.subway.utils.TestUtils.getRandomId(), name);
    }

    public static Line appendSectionByLine(final String lineName, final Section ...items) {
        return appendSectionByLine(lineName, 0, items);
    }

    public static Line appendSectionByLine(final String lineName, final int charge, final Section ...items) {
        Line line =  new Line(lineName, "");
        line.setAdditionalCharge(charge);

        for(Section section : items) {
            line.addSection(section);
        }

        return line;
    }
}
