package nextstep.subway.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class FactoryMethods {
    public static Station createStation(String name) {
        return Station.from(name);
    }

    public static Section createSection(Station upStation, Station downStation, int distance) {
        return Section.of(upStation, downStation, distance);
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance);
    }
}
