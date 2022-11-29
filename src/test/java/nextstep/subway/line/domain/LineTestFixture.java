package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

public class LineTestFixture {

    private static final int ZERO = 0;

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        return new Line(name, color, upStation, downStation, distance, ZERO);
    }

    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance, int lineFare) {
        return new Line(name, color, upStation, downStation, distance, lineFare);
    }
}
